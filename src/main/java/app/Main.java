package app;

import app.index.*;
import app.login.*;
import app.user.*;
import app.util.*;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Main {

    public static UserDao userDao;

    public static void main(String[] args) {

        userDao = new UserDao();

        staticFiles.location("/static");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*", Filters.addTrailingSlashes);
        before("*",Filters.handleLocaleChange);

        // Get and Post
        get(Path.Web.INDEX, IndexController.serveIndexPage);
        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        post(Path.Web.LOGIN,LoginController.handleLoginPost);
        post(Path.Web.LOGOUT,LoginController.handleLogoutPost);
        get("*", ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",Filters.addGzipHeader);

    }
}