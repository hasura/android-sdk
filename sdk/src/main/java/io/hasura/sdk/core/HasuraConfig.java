package io.hasura.sdk.core;

/**
 * Created by jaison on 23/01/17.
 */

public class HasuraConfig {

    public static String PROJECT_NAME;

    public static class BASE_URL {
        public static final String AUTH = "https://auth." + PROJECT_NAME + ".hasura-app.io";
        public static final String DB = "https://data." + PROJECT_NAME + ".hasura-app.io";
    }

    public static class URL {
        public static final String VERSION = "v1";
        public static final String LOGIN_MOBILE = "/otp-login";
        public static final String SIGNUP_MOBILE = "/otp-signup";
        public static final String LOGIN = "/login";
        public static final String SIGNUP = "/signup";
        public static final String LOGOUT = "/user/logout";
        public static final String QUERY = VERSION + "/query";
        public static final String ACCOUNT_INFO = "/user/account/info";
    }
}
