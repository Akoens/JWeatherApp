package app.controller;

import spark.*;

import static app.Main.weatherDataStore;

public class ApiController {
    public static Route handleApiExport = (Request request, Response response) -> {
        response.status(4);
        if (weatherDataStore.parseCSV()){
            response.status(500);
        }else{
            response.status(200);
        }
        return null;
    };

}
