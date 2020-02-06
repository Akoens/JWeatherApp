package app.util;

import lombok.*;

public class Path {
    public static class Web {
        @Getter public static final String INDEX = "/";
        @Getter public static final String GENERAL = "/general/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String SIGNUP = "/signup/";
        @Getter public static final String EXPORT = "/export/";
        @Getter public static final String API = "/api/";
        @Getter public static final String FORBIDDEN = "/forbidden/";
        @Getter public static final String EURASIA = "/export/eurasia";
    }

    public static class Template {
        public static final String INDEX = "/velocity/index/index.vm";
        public static final String GENERAL = "/velocity/general/general.vm";
        public static final String LOGIN = "/velocity/login/login.vm";
        public static final String SIGNUP = "/velocity/signup/signup.vm";
        public static final String EXPORT = "/velocity/export/export.vm";
        public static final String API = "/velocity/api/api.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public static final String FORBIDDEN = "/velocity/forbidden.vm";
    }
}
