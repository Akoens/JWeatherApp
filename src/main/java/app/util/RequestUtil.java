package app.util;

import spark.*;

public class RequestUtil {

    public static String getQueryEmail(Request request) { return request.queryParams("email").toLowerCase().trim(); }

    public static int getQueryAuthLevel(Request request) {return  Integer.parseInt(request.queryParams("authLevel"));}

    public static String getQueryPassword(Request request) {
        return request.queryParams("password").trim();
    }

    public static String getQueryConfirmPassword(Request request){return request.queryParams("confirmPassword");}

    public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
    }

    public static int getSessionAuthLevel(Request request) {
        return request.session().attribute("authLevel");
    }

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

    public static Integer removeSessionAttrAuthLevel(Request request) {
        Integer authLevel = request.session().attribute("authLevel");
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
