package io.hasura.sdk.utils;

/**
 * Created by jaison on 23/01/17.
 */

public class HasuraConfig {

    public static class URL {
        public static final String AUTH = "https://auth.warble80.hasura-app.io";
        public static final String DB = "https://data.warble80.hasura-app.io";
        public static final String VERSION = "v1";

        public static final String LOGIN = "login";
        public static final String REGISTER = "signup";
        public static final String LOGOUT = "user/logout";
        public static final String QUERY = VERSION + "/query";
    }
}
