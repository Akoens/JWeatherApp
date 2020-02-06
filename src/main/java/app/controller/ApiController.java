package app.controller;

import spark.*;

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

}
