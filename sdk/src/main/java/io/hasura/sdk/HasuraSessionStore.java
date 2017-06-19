package io.hasura.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by jaison on 30/03/17.
 */

public class HasuraSessionStore {

    private static SharedPreferences prefs;
    private static final String PREF_NAME = "HasuraUserStore";

    private static final String KEY_USER_ID = "IdKey";
    private static final String KEY_USER_TOKEN = "AuthTokenKey";
    private static final String KEY_USER_USERNAME = "UsernameKey";
    private static final String KEY_USER_MOBILE = "MobileKey";
    private static final String KEY_USER_EMAIL = "EmailKey";
    private static final String KEY_USER_ROLES = "UserRolesKey";
    private static final String KEY_USER_PASSWORD = "UserPasswordKey";

    public static void initialise(Context context) {
        SharedPrefManager.getInstance().initialize(context);
        prefs = SharedPrefManager.getInstance().getPref(PREF_NAME);
    }

    public static void saveUser(HasuraUser hasuraUser) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_USER_ID, hasuraUser.getId());
        editor.putString(KEY_USER_TOKEN, hasuraUser.getAuthToken());
        editor.putString(KEY_USER_USERNAME, hasuraUser.getUsername());
        editor.putString(KEY_USER_MOBILE, hasuraUser.getMobile());
        editor.putString(KEY_USER_EMAIL, hasuraUser.getEmail());

        String jsonArray = new Gson().toJson(hasuraUser.getRoles());
        editor.putString(KEY_USER_ROLES, jsonArray);

        editor.putString(KEY_USER_PASSWORD, hasuraUser.getPassword());
        editor.apply();
    }

    public static void updateUserWithSavedData(HasuraUser hasuraUser) {
        int id = prefs.getInt(KEY_USER_ID,-1);

        if (id == -1) {
            return;
        }

        String authToken = prefs.getString(KEY_USER_TOKEN,null);
        String username = prefs.getString(KEY_USER_USERNAME,null);
        String mobile = prefs.getString(KEY_USER_MOBILE,null);
        String email = prefs.getString(KEY_USER_EMAIL,null);

        String roleString = prefs.getString(KEY_USER_ROLES,null);
        String[] roles = new Gson().fromJson(roleString, String[].class);

        String password = prefs.getString(KEY_USER_PASSWORD,null);

        hasuraUser.setId(id);
        hasuraUser.setAuthToken(authToken);
        hasuraUser.setUsername(username);
        hasuraUser.setMobile(mobile);
        hasuraUser.setEmail(email);
        hasuraUser.setRoles(roles);
        hasuraUser.setPassword(password);
    }

    public static void deleteSavedUser() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
