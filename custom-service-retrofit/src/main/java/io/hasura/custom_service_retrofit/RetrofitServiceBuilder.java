package io.hasura.custom_service_retrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.hasura.sdk.CustomServiceBuilder;
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
 * Created by jaison on 11/06/17.
 */

public class RetrofitServiceBuilder implements CustomServiceBuilder {

    private static RetrofitServiceBuilder instance;

    public static RetrofitServiceBuilder create() {
        if (instance == null)
            instance = new RetrofitServiceBuilder();
        return instance;
    }

    public <K> K getApiInterface(HasuraTokenInterceptor hasuraTokenInterceptor, String baseUrl, final HashMap<String, String> additionalHeaders, Class<K> clazz) {
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

        return retrofit.create(clazz);
    }

}
