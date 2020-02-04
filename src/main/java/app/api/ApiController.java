package app.api;

import app.util.ViewUtil;
import spark.*;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import app.util.Path;
import static app.Main.userDao;

public class ApiController {
    public static Route serveApiPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllEmails());
        return ViewUtil.render(request, model, app.util.Path.Template.API);
    };

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
