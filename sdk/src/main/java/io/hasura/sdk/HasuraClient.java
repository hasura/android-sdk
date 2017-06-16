package io.hasura.sdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by jaison on 23/01/17.
 */

public class HasuraClient {

    private String baseDomain;
    private String protocol;
    private String apiVersion;

    private static HasuraClient instance;

    private static HasuraUser currentUser;

    public static HasuraClient getInstance() {
        return instance;
    }

    public static <K> K useCustomService(Class<K> clzz) {
        return currentUser.useCustomService(clzz);
    }

    public HasuraQuery.Builder useDataService() {
        return currentUser.useDataService();
    }

    public HasuraQuery.Builder useQueryTemplateService(String templateName) {
        return currentUser.useQueryTemplateService(templateName);

    }

    public static HasuraUser getNewUser() {
        return new HasuraUser();
    }

    public static HasuraUser currentUser() {
        return HasuraSessionStore.getSavedUser();
    }

    public static void setCurrentUser(HasuraUser user) {
        HasuraSessionStore.saveUser(user);
    }

    public HasuraClient(String baseDomain) {
        this.baseDomain = baseDomain;
        this.protocol = "https";
        this.apiVersion = "v1";
    }

    public static HasuraClient setProjectName(String projectName) {
        instance = new HasuraClient(projectName + ".hasura-app.io");
        return instance;
    }

    public static HasuraClient setCustomBaseDomain(String baseDomain) throws HasuraInitException {
        if (baseDomain.endsWith("/")) {
            throw new HasuraInitException("Custom base domain must not end with '/'");
        }
        instance = new HasuraClient(baseDomain);
        return instance;
    }

    //TODO: another name -enableInSecureConnection
    public HasuraClient enableOverHttp() {
        this.protocol = "http";
        return this;
    }

    public HasuraClient enableLogs() {
        HasuraConfig.SDK.enableLogs();
        return this;
    }

    public HasuraClient setCustomDefaultRole(String role) {
        HasuraConfig.USER.DEFAULT_ROLE = role;
        return this;
    }

    public HasuraClient setApiVersion(int versionNumber) {
        this.apiVersion = "v" + versionNumber;
        return this;
    }

    public void initialise(Context context) {
        HasuraConfig.BASE_DOMAIN = baseDomain;
        HasuraConfig.PROTOCOL = protocol;
        HasuraConfig.API_VERSION = apiVersion;
        HasuraSessionStore.initialise(context);
    }

    HashMap<Class, CustomService> customServiceMap = new HashMap<>();

    public <K> HasuraClient addCustomService(@NonNull CustomService<K> cs) throws HasuraInitException {
        if (customServiceMap.containsKey(cs.getClazz())) {
            throw new HasuraInitException("Custom service with name " + cs.getClazz().getName() + " is already added to HasuraClient");
        }
        cs.setBaseDomain(protocol, baseDomain);
        customServiceMap.put(cs.getClazz(), cs);
        return this;
    }

    @Nullable
    public <K> CustomService<K> getService(Class<K> clazz) {
        CustomService<K> cs = customServiceMap.get(clazz);
        return cs;
    }
}
