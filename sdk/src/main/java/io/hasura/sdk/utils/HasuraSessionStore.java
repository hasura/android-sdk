package io.hasura.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.hasura.sdk.auth.response.HasuraSession;

/**
 * Created by jaison on 30/03/17.
 */

public class HasuraSessionStore {

    final static String PREFS_NAME = "HasuraPreferenceFile";
    final static String PREFS_SESSION = "UserInfo";

    static SharedPreferences prefs;

    static void initialise(Context context) {
        SharedPrefManager.getInstance().initialize(context);
        prefs = SharedPrefManager.getInstance().getPref(PREFS_NAME);
    }

    /**
     * SESSION
     **/
    @Nullable
    private static HasuraSession getCurrentSession() {
        try{
            return new Gson().fromJson(prefs.getString(PREFS_SESSION,""),HasuraSession.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static void saveSession(HasuraSession session) {
        String jsonString = new Gson().toJson(session, HasuraSession.class);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_SESSION,jsonString);
        editor.apply();
    }

    public static void clearSession() {
        saveSession(null);
    }

    public static String getUserSession() {
        return getCurrentSession() != null ? getCurrentSession().getSessionId() : null;
    }

    public static int getUserId() {
        return getCurrentSession() != null ? getCurrentSession().getHasuraId() : -1;
    }

    public static String[] getUserRoles() {
        return getCurrentSession() != null ? getCurrentSession().getHasuraRoles() : null;
    }
}
