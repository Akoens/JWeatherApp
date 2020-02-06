package app.controller;

import app.util.Path;
import app.util.ViewUtil;
import spark.*;

import java.util.HashMap;
import java.util.Map;

public class DetailsController {

    public static Route serveDetailsPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("stationID", request.params(":stationID"));
        return ViewUtil.render(request, model, Path.Template.DETAILS);
    };

}
