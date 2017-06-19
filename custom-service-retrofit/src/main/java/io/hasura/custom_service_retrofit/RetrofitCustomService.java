package io.hasura.custom_service_retrofit;

import java.util.HashMap;

import io.hasura.sdk.service.CustomService;
import io.hasura.sdk.service.CustomServiceBuilder;

/**
 * Created by jaison on 16/06/17.
 */

public class RetrofitCustomService<K> extends CustomService<K> {

    Class<K> clazz;
    String serviceName;
    HashMap<String, String> additionalHeaders;

    private RetrofitCustomService(Builder builder, Class<K> clazz) {
        this.clazz = clazz;
        this.serviceName = builder.serviceName;
        this.additionalHeaders = builder.additionalHeaders;
    }

    @Override
    public Class<K> getClazz() {
        return clazz;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public HashMap<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    @Override
    public CustomServiceBuilder getServiceBuilder() {
        return RetrofitServiceBuilder.create();
    }

    public static final class Builder {

        String serviceName;
        String baseUrl;
        HashMap<String, String> additionalHeaders;

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
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

        public <K> RetrofitCustomService<K> build(Class<K> clazz) {
            return new RetrofitCustomService<>(this, clazz);
        }

    }
}
