package app.controller;

import app.Main;
import app.model.AfricanData;
import app.model.EurasianData;
import app.util.ViewUtil;
import spark.*;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import app.util.Path;

public class ApiController {

    public static Route handleApi = (Request request, Response response) -> {
        File saveDir = new File("src/main/resources/static/api");


        java.nio.file.Path tempFile = Files.createTempFile(saveDir.toPath(), "", "");

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        try (InputStream input = request.raw().getPart("file").getInputStream()) { // getPart needs to use same "name" as input field in form
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        logInfo(request, tempFile);
        response.redirect(Path.Web.API);
        return null;


    };

    public static Route handleStations = (Request request, Response response) -> {
        String resultJSON = "{\"status\":\"Bad request\",\"status_code\":400}";
        response.status(400);
        response.type("application/json");

        if (request.params(":continent") == null || request.params(":stationID") == null || request.params(":type") == null) {
            return resultJSON;
        }

        try {
            int stationID = Integer.parseInt(request.params(":stationID"));
            String type = request.params(":type");
            String continent = request.params(":continent");

            switch (continent) {
                case "africa":
                    switch (type) {
                        case "live":
                            AfricanData data = Main.weatherDataStore.getLatestAfricanEntry(stationID);
                            if (data == null)
                                return resultJSON;
                            response.status(200);
                            return data.toJSON();
                        case "historical":
                            AfricanData[] old = Main.weatherDataStore.getPastHourAfricanData(stationID);
                            if (old == null)
                                return resultJSON;
                            String json = "{\"data\":[";
                            for (AfricanData d : old)
                                if (d != null)
                                    json += d.toJSON() + ",";
                            if (old.length > 0)
                                json = json.substring(0, json.length()-1);
                            json += "]}";
                            response.status(200);
                            return json;
                    }
                    break;
                case "eurasia":
                    switch (type) {
                        case "historical":
                            EurasianData[] old = Main.weatherDataStore.getEurasianDataByDay(stationID, new Date());
                            if (old == null)
                                return resultJSON;
                            String json = "{\"data\":[";
                            for (EurasianData d : old)
                                if (d != null)
                                    json += d.toJSON() + ",";
                            if (old.length > 0)
                                json = json.substring(0, json.length()-1);
                            json += "]}";
                            response.status(200);
                            return json;
                    }
                    break;
            }

        } catch(NumberFormatException e){
            return resultJSON;
        }

        return resultJSON;
    };

    // methods used for logging
    private static void logInfo(Request request, java.nio.file.Path tempFile) throws IOException, ServletException {
        System.out.println("Uploaded file '" + getFileName(request.raw().getPart("file")) + "' saved as '" + tempFile.toString() + "'");
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
