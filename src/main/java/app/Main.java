package app;

import app.engine.WeatherDataProcessor;
import app.general.GeneralController;
import app.index.IndexController;
import app.login.LoginController;
import app.api.ApiController;
import app.net.WeatherClient;
import app.net.WeatherDataReceiver;
import app.settings.SettingsController;
import app.signup.SignupController;
import app.store.WeatherDataStore;
import app.user.UserDao;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static UserDao userDao;

    public static void main(String[] args) {

        try {
            userDao = new UserDao();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        port(4567);
        staticFiles.location("/static");
        staticFiles.expireTime(600L);

        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*", Filters.addTrailingSlashes);
        before("*", Filters.handleLocaleChange);

        //Authentication filters for accessing these web pages
        before(Path.Web.INDEX, Filters.handleForbiddenAuthentication);
        before(Path.Web.SETTINGS, Filters.handleForbiddenAuthentication);

        // Get and Post
        get(Path.Web.INDEX, IndexController.serveIndexPage);
        get(Path.Web.GENERAL, GeneralController.serveGeneralPage);
        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        get(Path.Web.SIGNUP, SignupController.serveSignupPage);
        get(Path.Web.SETTINGS, SettingsController.serveSettingsPage);
        get(Path.Web.API, ApiController.serveApiPage);

        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        post(Path.Web.API, ApiController.handleApi);
        get(Path.Web.FORBIDDEN, ViewUtil.forbidden);
        get("*", ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);


        WeatherDataProcessor processor = new WeatherDataProcessor(new WeatherDataStore());
        WeatherDataReceiver receiver = new WeatherDataReceiver(4433);
        receiver.addWeatherServerListener(processor);
        receiver.listen();
    }
}