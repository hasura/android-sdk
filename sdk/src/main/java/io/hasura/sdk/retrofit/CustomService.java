package io.hasura.sdk.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaison on 09/06/17.
 */

public class CustomService<K> {

    K apiInterface;

    public CustomService(K apiInterface) {
        this.apiInterface = apiInterface;
    }

    public K getInterface() {
        return apiInterface;
    }

    public static final class Builder {

        String serviceName;
        String protocol = "https://";
        String domainName = "hasura-app.io";
        Retrofit retrofit;

        private String getBaseUrl() {
            return protocol + serviceName + "." + domainName;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder customBaseDomain(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public Builder enableOverHttp() {
            this.protocol = "http://";
            return this;
        }

        public <K> CustomService<K> build(Class<K> clazz) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return new CustomService<>(retrofit.create(clazz));
        }

    }

}
