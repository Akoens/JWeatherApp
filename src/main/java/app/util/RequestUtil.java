package app.util;

import spark.*;

public class RequestUtil {

    public static String getQueryEmail(Request request) {
        return request.queryParams("email");
    }

    public static String getQueryPassword(Request request) {
        return request.queryParams("password");
    }

    public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
    }

    public static String getQueryPasswordConfirm(Request request){return request.queryParams("PasswordConfirm");}

    public static String removeSessionSignupError(Request request){
        String authLevel = request.session().attribute("signupError");
        request.session().removeAttribute("signupError");
        return authLevel;
    }

    public static boolean removeSessionAttrLoggedOut(Request request) {
        Object loggedOut = request.session().attribute("loggedOut");
        request.session().removeAttribute("loggedOut");
        return loggedOut != null;
    }

    public static String removeSessionAttrLoginRedirect(Request request) {
        String loginRedirect = request.session().attribute("loginRedirect");
        request.session().removeAttribute("loginRedirect");
        return loginRedirect;
    }

    public static String removeSessionAttrAuthLevel(Request request) {
        String authLevel = request.session().attribute("authLevel");
        request.session().removeAttribute("authLevel");
        return authLevel;
    }

    public static boolean clientAcceptsHtml(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
    }

    public static boolean clientAcceptsJson(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("application/json");
    }

}
