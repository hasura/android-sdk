package io.hasura.sdk;

import android.content.Context;

import java.util.HashMap;

import io.hasura.sdk.retrofit.CustomService;

/**
 * Created by jaison on 23/01/17.
 */

public class Hasura {

    private String baseDomain;
    private String protocol;
    private String apiVersion;

    private static Hasura instance;

    public static Hasura getInstance() {
        return instance;
    }

    public static HasuraUser currentUser() {
        return HasuraSessionStore.getSavedUser();
    }

    public Hasura(String baseDomain) {
        this.baseDomain = baseDomain;
        this.protocol = "https";
        this.apiVersion = "v1";
    }

    public static Hasura setProjectName(String projectName) {
        instance = new Hasura(projectName + ".hasura-app.io/");
        return instance;
    }

    public static Hasura setCustomBaseDomain(String baseDomain) {
        if (baseDomain.charAt(baseDomain.length() - 1) != '/')
            instance = new Hasura(baseDomain + "/");
        else instance = new Hasura(baseDomain);
        return instance;
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
    }

    HashMap<String, CustomService> customServiceMap = new HashMap<>();

    public <K> Hasura addCustomService(CustomService<K> cs) {
        customServiceMap.put(cs.getServiceName(), cs);
        return this;
    }

    public <K> CustomService<K> getService(String serviceName) {
        CustomService<K> cs = customServiceMap.get(serviceName);
        return cs;
    }
}
