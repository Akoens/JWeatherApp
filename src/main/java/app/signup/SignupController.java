package app.signup;

import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.Main.userDao;

public class SignupController {
    public static Route serveSignupPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllUserNames());
        return ViewUtil.render(request, model, Path.Template.SIGNUP);
    };
}
