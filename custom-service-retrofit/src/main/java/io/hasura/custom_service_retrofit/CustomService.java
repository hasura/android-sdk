package io.hasura.custom_service_retrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.hasura.sdk.HasuraConfig;
import io.hasura.sdk.HasuraTokenInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaison on 09/06/17.
 */

public class CustomService<K> {

    private K apiInterface;
    private String serviceName;
    private Class<K> clazz;
    private String baseUrl;
    private HashMap<String, String> additionalHeaders;

    public CustomService(String serviceName, String baseUrl, HashMap<String, String> additionalHeaders, Class<K> clazz) {
        this.clazz = clazz;
        this.baseUrl = baseUrl;
        this.serviceName = serviceName;
        this.additionalHeaders = additionalHeaders;
    }

    public String getServiceName() {
        return serviceName;
    }

    public K getInterface(HasuraTokenInterceptor hasuraTokenInterceptor) {
        if (apiInterface == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            if (HasuraConfig.SDK.isLoggingEnabled) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(logging);
            }

            clientBuilder.addInterceptor(hasuraTokenInterceptor);
            if (additionalHeaders != null) {
                clientBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder newRequestBuilder = request.newBuilder();
                        for (Map.Entry<String, String> header : additionalHeaders.entrySet()) {
                            newRequestBuilder.addHeader(header.getKey(), header.getValue());

                        }
                        return chain.proceed(newRequestBuilder.build());
                    }
                });
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            this.apiInterface = retrofit.create(clazz);
        }

        return apiInterface;
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

        public <K> CustomService<K> build(Class<K> clazz) {
            return new CustomService<>(serviceName, baseUrl, additionalHeaders, clazz);
        }

    }

}
