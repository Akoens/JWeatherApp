package app.util;

import lombok.*;

public class Path {
    public static class Web {
        @Getter public static final String INDEX = "/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String SIGNUP = "/signup/";
        @Getter public static final String EXPORT = "/export/";
        @Getter public static final String GENERAL = "/general/";
        @Getter public static final String SETTINGS = "/settings/";
        @Getter public static final String FORBIDDEN = "/forbidden/";
        @Getter public static final String API_EXPORT = "/api/export/";
        @Getter public static final String DETAILS = "/details/:stationID/";
        @Getter public static final String EXPORT_AFRICA = "/export/africa/";
        @Getter public static final String EXPORT_EURASIA = "/export/eurasia/";
        @Getter public static final String API_STATION = "/api/station/:continent/:stationID/:type/";
    }

    public static class Template {
        public static final String INDEX = "/velocity/index/index.vm";
        public static final String GENERAL = "/velocity/general/general.vm";
        public static final String LOGIN = "/velocity/login/login.vm";
        public static final String DETAILS = "/velocity/details/details.vm";
        public static final String SIGNUP = "/velocity/signup/signup.vm";
        public static final String EXPORT = "/velocity/export/export.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public static final String FORBIDDEN = "/velocity/forbidden.vm";
    }
}
