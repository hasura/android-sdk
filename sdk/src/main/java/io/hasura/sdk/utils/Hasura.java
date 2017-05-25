package io.hasura.sdk.utils;

import android.content.Context;

import io.hasura.sdk.auth.AuthService;
import io.hasura.sdk.db.DBService;

/**
 * Created by jaison on 23/01/17.
 */

public class Hasura {

    public static AuthService auth;
    public static DBService db;

    public static void initialise(Context context, String projectName) {
        HasuraConfig.PROJECT_NAME = projectName;

        HasuraSessionStore.initialise(context);
        auth = new AuthService(HasuraConfig.URL.AUTH);
        db = new DBService(HasuraConfig.URL.DB);
    }

}
