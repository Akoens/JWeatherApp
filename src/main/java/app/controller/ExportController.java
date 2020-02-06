package app.controller;

import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.removeSessionAttrAuthLevel;

public class ExportController {
    public static Route serveSettingsPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.EXPORT);
    };

    public static Route exportCSV = (Request request, Response response) -> {
        return null;
    };
}
