package io.hasura.sdk.utils;

import android.content.Context;

import io.hasura.sdk.auth.AuthService;
import io.hasura.sdk.utils.HasuraQuery.Condition;

/**
 * Created by jaison on 23/01/17.
 */

public class Hasura {

    public static AuthService auth;

    public static void initialise(Context context) {
        HasuraSessionStore.initialise(context);
        auth = new AuthService(HasuraConfig.URL.AUTH);
    }

}
