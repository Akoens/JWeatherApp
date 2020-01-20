package app.index;

import app.util.*;
import spark.*;
import java.util.*;
import static app.Main.userDao;

public class IndexController {
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllUserNames());
        return ViewUtil.render(request, model, Path.Template.INDEX);
    };
}
