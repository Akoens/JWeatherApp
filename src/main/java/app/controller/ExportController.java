package app.controller;

import app.Main;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.removeSessionAttrAuthLevel;

public class ExportController {
    public static Route serveExportPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.EXPORT);
    };
    public static Route handleAfricaExport = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return Main.weatherDataStore.getAfricanCSV();
    };

    public static Route handleEurasiaExport = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return Main.weatherDataStore.getEurasianCSV();
    };
}