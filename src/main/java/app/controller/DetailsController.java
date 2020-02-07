package app.controller;

import app.Main;
import app.model.Station;
import app.util.Path;
import app.util.StringUtil;
import app.util.ViewUtil;
import spark.*;

import java.util.HashMap;
import java.util.Map;

public class DetailsController {

    public static Route serveDetailsPage = (Request request, Response response) -> {
        String continent = request.params(":continent");
        if (request.params(":stationID") == null || continent == null) {
            response.redirect("/", 302);
            return null;
        }

        continent = continent.toLowerCase();
        if (!continent.equals("africa") && !continent.equals("europe") && !continent.equals("asia")) {
            response.redirect("/", 302);
            return null;
        }

        try {
            int stationID = Integer.parseInt(request.params(":stationID"));
            if (!Main.weatherDataStore.getStations().containsKey(stationID)) {
                response.redirect("/", 302);
                return null;
            }
            Station station = Main.weatherDataStore.getStations().get(stationID);
            Map<String, Object> model = new HashMap<>();
            model.put("stationID", stationID);
            model.put("continent", continent);
            model.put("name", StringUtil.capitalize(station.getName().toLowerCase()));
            model.put("country", StringUtil.capitalize(station.getCountry().toLowerCase()));
            model.put("temp", "unknown");
            if (continent.equals("africa")) {
                model.put("temp", String.format("%.2f", Main.weatherDataStore.getLatestAfricanEntry(stationID).getHeatIndex()));
                model.putIfAbsent("temp", "unknown");
                return ViewUtil.render(request, model, Path.Template.DETAILS_AFRICA);
            }
            return ViewUtil.render(request, model, Path.Template.DETAILS_EURASIA);
        } catch(NumberFormatException ignored){
        }
        response.redirect("/", 302);
        return null;
    };

}
