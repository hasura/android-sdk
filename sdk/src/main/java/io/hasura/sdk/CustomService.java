package io.hasura.sdk;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by jaison on 09/06/17.
 */

public class CustomService<K> {

    private K apiInterface;

    private HashMap<HasuraTokenInterceptor, K> apiInterfaceMap = new HashMap<>();

    private String serviceName;
    private Class<K> clazz;
    private String baseUrl;
    private HashMap<String, String> additionalHeaders;
    private CustomServiceBuilder serviceBuilder;

    public CustomService(Builder builder, Class<K> clazz) {
        this.clazz = clazz;
        this.baseUrl = builder.baseUrl;
        this.serviceName = builder.serviceName;
        this.additionalHeaders = builder.additionalHeaders;
        this.serviceBuilder = builder.serviceBuilder;
    }

    public Class<K> getClazz() {
        return clazz;
    }

    @NonNull
    public K getInterface(HasuraTokenInterceptor hasuraTokenInterceptor) {
        if (!apiInterfaceMap.containsKey(hasuraTokenInterceptor)) {
            Log.i("ApiInterfaceNull",hasuraTokenInterceptor.toString());
            K apiInterface = serviceBuilder.getApiInterface(hasuraTokenInterceptor, baseUrl, additionalHeaders, clazz);
            apiInterfaceMap.put(hasuraTokenInterceptor, apiInterface);
            return apiInterface;
        }

        return apiInterfaceMap.get(hasuraTokenInterceptor);
    }

    public static final class Builder {

        String serviceName;
        String baseUrl;
        HashMap<String, String> additionalHeaders;
        CustomServiceBuilder serviceBuilder;

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder setServiceBuilder(CustomServiceBuilder serviceBuilder) {
            this.serviceBuilder = serviceBuilder;
            return this;
        }

        public Builder baseUrl(String domainName) {
            this.baseUrl = domainName;
            return this;
        }

        public Builder addHeader(String name, String value) {
            if (additionalHeaders == null) {
                additionalHeaders = new HashMap<>();
                additionalHeaders.put(name, value);
            }
            return this;
        }

        public <K> CustomService<K> build(Class<K> clazz) {
            return new CustomService<>(this, clazz);
        }

    }

}
