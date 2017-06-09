package io.hasura.sdk.core;

import android.content.Context;

import java.util.HashMap;

import io.hasura.sdk.auth.HasuraUser;

/**
 * Created by jaison on 23/01/17.
 */

public class Hasura {

    private String baseDomain;
    private String protocol;
    private String apiVersion;

    private static HasuraUser currentUser;

    public static HasuraUser currentUser() {
        return currentUser;
    }

    public Hasura(String baseDomain) {
        this.baseDomain = baseDomain;
        this.protocol = "https";
        this.apiVersion = "v1";
    }

    public static Hasura setProjectName(String projectName) {
        return new Hasura(projectName + ".hasura-app.io");
    }

    public static Hasura setCustomBaseDomain(String baseDomain) {
        return new Hasura(baseDomain);
    }

    //TODO: another name -enableInSecureConnection
    public Hasura enableOverHttp() {
        this.protocol = "http";
        return this;
    }

    public Hasura enableLogs() {
        HasuraConfig.SDK.enableLogs();
        return this;
    }

    public Hasura setCustomDefaultRole(String role) {
        HasuraConfig.USER.DEFAULT_ROLE = role;
        return this;
    }

    public Hasura setApiVersion(int versionNumber) {
        this.apiVersion = "v" + versionNumber;
        return this;
    }

    public void initialise(Context context) {
        HasuraConfig.BASE_DOMAIN = baseDomain;
        HasuraConfig.PROTOCOL = protocol;
        HasuraConfig.API_VERSION = apiVersion;
        HasuraSessionStore.initialise(context);
        currentUser = HasuraSessionStore.getSavedUser();
    }
}
