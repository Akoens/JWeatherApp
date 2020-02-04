package app.util;

import spark.*;
import static app.util.RequestUtil.*;
import static spark.Spark.halt;

public class Filters {

    // If a app.user manually manipulates paths and forgets to add
    // a trailing slash, redirect the app.user to the correct path
    public static Filter addTrailingSlashes = (Request request, Response response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };

    // Locale change can be initiated from any page
    // The locale is extracted from the request and saved to the app.user's session
    public static Filter handleLocaleChange = (Request request, Response response) -> {
        if (getQueryLocale(request) != null) {
            request.session().attribute("locale", getQueryLocale(request));
            response.redirect(request.pathInfo());
        }
    };

    public static Filter handleForbiddenAuthentication = (Request request, Response response) -> {
        if((request.session().attribute("authLevel") == null) || ((int)request.session().attribute("authLevel") < 1)){
            response.redirect(Path.Web.FORBIDDEN);
        }
    };

    // Enable GZIP for all responses
    public static Filter addGzipHeader = (Request request, Response response) -> {
        response.header("Content-Encoding", "gzip");
    };

}
