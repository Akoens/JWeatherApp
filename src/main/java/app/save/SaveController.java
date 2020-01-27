package app.save;

import app.util.*;
import spark.*;
import spark.staticfiles.

import java.io.File;
import java.util.*;
import static app.Main.userDao;

public class SaveController {
    static File file = null;
    static String filename = null;
    final static String filepath = "/public/save"/;

    public static Route serveSavePage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllUserNames());
        return ViewUtil.render(request, model, Path.Template.SAVE);
    };

    public static Route handleSave = (Request request, Response response) -> {
        request.params(String)
        file = new File(filepath+)
        return null;

    };
}
