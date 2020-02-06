package app.util;

import org.apache.velocity.app.*;
import org.eclipse.jetty.http.*;
import spark.*;
import spark.template.velocity.*;
import java.util.*;
import static app.util.RequestUtil.*;

public class ViewUtil {

    // Renders a template given a model and a request
    // The request is needed to check the app.user session for language settings
    // and to see if the app.user is logged in
    public static String render(Request request, Map<String, Object> model, String templatePath) {
        model.put("currentUser", getSessionCurrentUser(request));
        int autLevel = removeSessionAttrAuthLevel(request);
        model.put("authLevel", autLevel);
        request.session().attribute("authLevel", autLevel);
        model.put("WebPath", Path.Web.class); // Access application URLs from templates
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }

    public static Route notAcceptable = (Request request, Response response) -> {
        response.status(HttpStatus.NOT_ACCEPTABLE_406);
        return "ERROR_406_NOT_ACCEPTABLE";
    };

    public static Route notFound = (Request request, Response response) -> {
        response.status(HttpStatus.NOT_FOUND_404);
        return render(request, new HashMap<>(), Path.Template.NOT_FOUND);
    };

    public static Route forbidden = (Request request, Response response) -> {
        response.status(HttpStatus.FORBIDDEN_403);
        return render(request, new HashMap<>(), Path.Template.FORBIDDEN);
    };

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
}
