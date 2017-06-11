package io.hasura.sdk;

import java.util.HashMap;

/**
 * Created by jaison on 09/06/17.
 */

public class CustomService<K> {

    private K apiInterface;
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

    public String getServiceName() {
        return serviceName;
    }

    public K getInterface(HasuraTokenInterceptor hasuraTokenInterceptor) {
        if (apiInterface == null) {
            this.apiInterface = serviceBuilder.getApiInterface(hasuraTokenInterceptor, baseUrl, additionalHeaders, clazz);
        }

        return apiInterface;
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
