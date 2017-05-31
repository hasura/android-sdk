package io.hasura.sdk.auth;

import android.content.Context;

import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.HasuraSessionStore;
import io.hasura.sdk.core.HasuraConfig;

/**
 * Created by jaison on 23/01/17.
 */

public class Hasura {

    private static HasuraUser currentUser;

    public static HasuraUser currentUser() {
        return currentUser;
    }

    public static void initialise(Context context, String projectName) {
        HasuraConfig.PROJECT_NAME = projectName;
        HasuraSessionStore.initialise(context);
        currentUser = HasuraSessionStore.getSavedUser();
    }
}
