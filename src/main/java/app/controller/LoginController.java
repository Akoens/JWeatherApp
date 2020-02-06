package app.controller;

import app.util.*;
import spark.*;
import java.util.*;

import static app.Main.userStore;
import static app.util.RequestUtil.*;

public class LoginController {

    public static Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        if (!UserController.authenticate(getQueryEmail(request), getQueryPassword(request))) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }
        String userEmail = getQueryEmail(request);
        String loginRedirect = request.session().attribute("loginRedirect");
        int authLevel = userStore.getUserByEmail(userEmail).getAuthLevel();

        model.put("authenticationSucceeded", true);
        model.put("authLevel", authLevel);

        request.session().attribute("currentUser", userEmail);
        request.session().attribute("authLevel", authLevel);
        if (loginRedirect != null) {
            response.redirect(loginRedirect);
        }
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().removeAttribute("authLevel");
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.GENERAL);
        return null;
    };

}
