package app.util;

import lombok.*;

public class Path {
    public static class Web {
        @Getter public static final String INDEX = "/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String API = "/api/";

    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String API = "/velocity/api/api.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
    }
}
