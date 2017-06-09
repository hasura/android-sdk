package io.hasura.sdk.core;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jaison on 08/06/17.
 */

public class OkHttpClientProvider {

    private OkHttpClientProvider() {}
    private static HashMap<HasuraTokenInterceptor, OkHttpClient> clientMap = new HashMap<>();
    private static OkHttpClient staticClient;

    public static OkHttpClient getClientForTokenInterceptor(HasuraTokenInterceptor interceptor) {
        if (clientMap.containsKey(interceptor.getRole()))
            return clientMap.get(interceptor);

        OkHttpClient newClient = getHttpClient(interceptor);
        clientMap.put(interceptor, newClient);
        return newClient;
    }

    public static OkHttpClient getHttpClient() {
        if (staticClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (HasuraConfig.SDK.isLoggingEnabled) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }
            staticClient = builder.build();
        }
        return staticClient;
    }

    private static OkHttpClient getHttpClient(HasuraTokenInterceptor tokenInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor);
        if (HasuraConfig.SDK.isLoggingEnabled) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

}
