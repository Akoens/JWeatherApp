package app.util;

import lombok.*;

public class Path {
    public static class Web {
        @Getter public static final String INDEX = "/app/index/";
        @Getter public static final String LOGIN = "/app/login/";
        @Getter public static final String LOGOUT = "/logout/";

    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
    }
}
