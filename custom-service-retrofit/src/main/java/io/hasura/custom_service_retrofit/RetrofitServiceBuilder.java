package io.hasura.custom_service_retrofit;

import android.util.Log;

import java.util.HashMap;

import io.hasura.sdk.service.CustomServiceBuilder;
import io.hasura.sdk.HasuraTokenInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaison on 11/06/17.
 */

public class RetrofitServiceBuilder implements CustomServiceBuilder {

    private static String TAG = "RetrofitBuilder";
    private static RetrofitServiceBuilder instance;

    public static RetrofitServiceBuilder create() {
        if (instance == null)
            instance = new RetrofitServiceBuilder();
        return instance;
    }

    public <K> K getApiInterface(HasuraTokenInterceptor hasuraTokenInterceptor, String baseUrl, final HashMap<String, String> additionalHeaders, Class<K> clazz) {
        if (clazz == null) {
            Log.i(TAG, "Class is null");
        } else {
            Log.i(TAG, "Class :" + clazz.getName());
        }

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(hasuraTokenInterceptor);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.i(TAG,"Built with : "+ hasuraTokenInterceptor.toString());

        return retrofit.create(clazz);
    }

}
