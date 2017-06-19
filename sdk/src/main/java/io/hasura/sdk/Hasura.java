package io.hasura.sdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import io.hasura.sdk.exception.HasuraInitException;
import io.hasura.sdk.service.CustomService;

/**
 * Created by jaison on 19/06/17.
 */

public class Hasura {

    private static Hasura instance;

    private ProjectConfig projectConfig;
    private Boolean shouldEnableLogs = false;
    private HasuraClient client;

    public static Hasura setProjectConfig(ProjectConfig config) {
        instance = new Hasura(config);
        return instance;
    }

    public static HasuraClient getClient() {
        return instance.client;
    }

    private Hasura(ProjectConfig config) {
        this.projectConfig = config;
    }

    public Hasura enableLogs() {
        this.shouldEnableLogs = true;
        return this;
    }

    public void initialise(Context context) {
        //Load session store
        HasuraSessionStore.initialise(context);
        //Load client
        this.client = new HasuraClient.Builder()
                .setProjectConfig(projectConfig)
                .shouldEnableLogs(shouldEnableLogs)
                .setCustomServices(customServiceMap)
                .build();

    }

    private HashMap<Class, CustomService> customServiceMap = new HashMap<>();

    public <K> Hasura addCustomService(@NonNull CustomService<K> cs) throws HasuraInitException {
        if (customServiceMap.containsKey(cs.getClazz())) {
            throw new HasuraInitException("Custom service with name " + cs.getClazz().getName() + " is already added to HasuraClient");
        }
        cs.setBaseDomain(projectConfig.getCustomServiceUrl(cs.getServiceName()));
        customServiceMap.put(cs.getClazz(), cs);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <K> CustomService<K> getService(Class<K> clazz) {
        return customServiceMap.get(clazz);
    }
}
