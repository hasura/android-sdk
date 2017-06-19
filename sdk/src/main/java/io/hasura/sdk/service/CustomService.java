package io.hasura.sdk.service;

import android.support.annotation.NonNull;

import java.util.HashMap;

import io.hasura.sdk.HasuraTokenInterceptor;

/**
 * Created by jaison on 09/06/17.
 */

public abstract class CustomService<K> {
    private HashMap<HasuraTokenInterceptor, K> apiInterfaceMap = new HashMap<>();

    private String serviceName;
    private Class<K> clazz;
    private HashMap<String, String> additionalHeaders;
    private CustomServiceBuilder serviceBuilder;
    private String baseUrl;

    public abstract Class<K> getClazz();

    public abstract String getServiceName();

    public abstract HashMap<String, String> getAdditionalHeaders();

    public abstract CustomServiceBuilder getServiceBuilder();

    public void setBaseDomain(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public CustomService() {
        this.clazz = getClazz();
        this.serviceName = getServiceName();
        this.additionalHeaders = getAdditionalHeaders();
        this.serviceBuilder = getServiceBuilder();
    }

    @NonNull
    public K getInterface(HasuraTokenInterceptor hasuraTokenInterceptor) {
        if (!apiInterfaceMap.containsKey(hasuraTokenInterceptor)) {
            K apiInterface = serviceBuilder.getApiInterface(hasuraTokenInterceptor, baseUrl, additionalHeaders, clazz);
            apiInterfaceMap.put(hasuraTokenInterceptor, apiInterface);
            return apiInterface;
        }

        return apiInterfaceMap.get(hasuraTokenInterceptor);
    }
}
