package app;

import app.controller.*;
import app.database.Database;
import app.handler.WeatherDataHandler;
import app.net.WeatherDataReceiver;
import app.store.WeatherDataStore;
import app.store.UserStore;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static UserStore userStore;
    public static WeatherDataStore weatherDataStore;

    public static void main(String[] args) {
        //Setup sqlite database
        Connection connection = Database.getSQLiteConnection();
        if (connection == null) {
            System.err.println("ERROR: No database connection, ceasing operation");
            System.exit(1);
        }

        //Setup user store
        try {
            userStore = new UserStore(connection);
            userStore.createTables();
            userStore.insertTestUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR: Could not create database tables");
        }

        //Setup weather data receiver
        weatherDataStore = new WeatherDataStore();
        weatherDataStore.touchDirectories();
        weatherDataStore.cleanup();
        WeatherDataHandler handler = new WeatherDataHandler(weatherDataStore);
        WeatherDataReceiver receiver = new WeatherDataReceiver(4433);
        receiver.addWeatherServerListener(handler);
        receiver.listen();

        port(4567);
        staticFiles.location("/static");
        staticFiles.expireTime(600L);

        enableDebugScreen();

        //Setup before-filters (called before each get/post)
        before("*", Filters.addTrailingSlashes);

        //Authentication filters for accessing these web pages
        //before(Path.Web.INDEX, Filters.handleLoginAuthentication);
        //before(Path.Web.SETTINGS, Filters.handleLoginAuthentication);
        before("*", Filters.handleLoginAuthentication);

        //Get and Post
        get(Path.Web.INDEX, IndexController.serveIndexPage);
        get(Path.Web.GENERAL, GeneralController.serveGeneralPage);
        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        get(Path.Web.DETAILS, DetailsController.serveDetailsPage);
        get(Path.Web.SIGNUP, SignupController.serveSignupPage);
        get(Path.Web.SETTINGS, SettingsController.serveSettingsPage);

        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.DETAILS, DetailsController.serveDetailsPage);
        post(Path.Web.SIGNUP, SignupController.handleSignupPost);
        get(Path.Web.FORBIDDEN, ViewUtil.forbidden);
        get("*", ViewUtil.notFound);

        //Setup after-filters (called after each get/post)
        after("*", Filters.addGzipHeader);
    }
}