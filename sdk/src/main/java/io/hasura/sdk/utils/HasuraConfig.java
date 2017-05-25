package io.hasura.sdk.utils;

import android.graphics.Path;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jaison on 23/01/17.
 */

public class HasuraConfig {

    static String PROJECT_NAME;


    //TODO: Read project config from a json file
//    static {
//        ClassLoader cl = HasuraConfig.class.getClass().getClassLoader();
//        File file = new File(cl.getResource("./examples/hasura.json").getFile());
//        try {
//            FileReader fr = new FileReader("~/Documents/Hasura/Android_SDK/examples/hasura.json");
//            JsonParser parser = new JsonParser();
//            JsonElement jsonElement = parser.parse(fr);
//            JsonObject jsonObject = jsonElement.getAsJsonObject();
//            PROJECT_NAME = jsonObject.get("ProjectName").getAsString();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//        PROJECT_NAME = "hello70";
//    }

    public static class URL {


        public static final String AUTH = "https://auth." + PROJECT_NAME + ".hasura-app.io";
        public static final String DB = "https://data." + PROJECT_NAME + ".hasura-app.io";
        public static final String VERSION = "v1";

        public static final String LOGIN = "login";
        public static final String REGISTER = "signup";
        public static final String LOGOUT = "user/logout";
        public static final String QUERY = VERSION + "/query";
    }
}
