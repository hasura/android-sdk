package io.hasura.sdk.core;

/**
 * Created by jaison on 23/01/17.
 */

public class HasuraConfig {

    public static class USER {
        public static String DEFAULT_ROLE = "user";
    }

    public static class SDK {
        public static Boolean isLoggingEnabled = false;

        public static void enableLogs() {
            isLoggingEnabled = true;
        }
    }

    static String BASE_DOMAIN;
    static String PROTOCOL;
    static String API_VERSION;

    public static String getCustomServiceURL(String serviceName) {
        return PROTOCOL + "://" + serviceName + "." + BASE_DOMAIN;
    }

    public static class BASE_URL {
        public static final String AUTH = PROTOCOL + "://auth." + BASE_DOMAIN;
        public static final String DB = PROTOCOL + "://data." + BASE_DOMAIN;
    }

    public static class URL {

        public static final String QUERY = "/" + API_VERSION + "/query";
        public static final String QUERY_TEMPLATE = "/" + API_VERSION + "/template";

        public static final String LOGIN_MOBILE = "/otp-login";
        public static final String SIGNUP_MOBILE = "/otp-signup";
        public static final String LOGIN = "/login";
        public static final String SIGNUP = "/signup";
        public static final String LOGOUT = "/user/logout";

        public static final String ACCOUNT_INFO = "/user/account/info";
    }
}
