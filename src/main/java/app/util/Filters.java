package app.util;

import spark.*;
import static app.util.RequestUtil.*;

public class Filters {

    // If a app.user manually manipulates paths and forgets to add
    // a trailing slash, redirect the app.user to the correct path
    public static Filter addTrailingSlashes = (Request request, Response response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };


    public static Filter handleLoginAuthentication = (Request request, Response response) -> {
        String pathInfo = request.pathInfo();
        if ((request.session().attribute("currentUser") == null) && !(request.pathInfo().equals(Path.Web.LOGIN) || request.pathInfo().equals(Path.Web.GENERAL))) {
            request.session().attribute("loginRedirect", pathInfo);
            response.redirect(Path.Web.LOGIN);
        }
        if((pathInfo.equals(Path.Web.INDEX) || pathInfo.equals(Path.Web.EXPORT))&&((request.session().attribute("authLevel") == null) || ((int)request.session().attribute("authLevel") < 1))){
            response.redirect(Path.Web.GENERAL);
        }
    };

    // Enable GZIP for all responses
    public static Filter addGzipHeader = (Request request, Response response) -> {
        response.header("Content-Encoding", "gzip");
    };

}
