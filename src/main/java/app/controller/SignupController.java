package app.controller;

import app.util.Path;
import app.util.RequestUtil;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.removeSessionSignupError;

public class SignupController {
    public static Route serveSignupPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("authLevel", removeSessionSignupError(request));
        return ViewUtil.render(request, model, Path.Template.SIGNUP);
    };

    public static Route handleSignupPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        //Password checks
        //TODO
        if(!(RequestUtil.getQueryPassword(request).equals(RequestUtil.getQueryPasswordConfirm(request)))){
            model.put("SignupError", "The passwords are not the same");
            return ViewUtil.render(request, model, Path.Template.SIGNUP);
        }
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };
}
