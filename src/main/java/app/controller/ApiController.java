package app.controller;


import app.Main;
import app.model.AfricanData;
import app.model.EurasianData;
import spark.*;

import java.util.Date;

import static app.Main.weatherDataStore;

public class ApiController {

    public static Route handleApiExport = (Request request, Response response) -> {
        response.type("application/json");
        if (weatherDataStore.parseCSV()){
            response.status(200);
            return "{\"status\":\"Ok\",\"status_code\":200}";
        }
        response.status(500);
        return "{\"status\":\"Internal server error\",\"status_code\":500}";
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

}
