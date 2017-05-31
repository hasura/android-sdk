package io.hasura.sdk.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.hasura.sdk.auth.HasuraTokenInterceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jaison on 23/05/17.
 */

public abstract class CoreApiService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient httpClient;
    private String baseUrl;
    public static final Gson gson = new GsonBuilder().create();

    public CoreApiService(String baseUrl, OkHttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public CoreApiService(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HasuraTokenInterceptor())
                .addInterceptor(logging)
                .build();
        this.baseUrl = baseUrl;
        this.httpClient = okHttpClient;
    }

    protected okhttp3.Call makeGetCall(String url) {
        return makeCall(url, null);
    }

    protected okhttp3.Call makePostCall(String url, @NonNull Object postBody) {
        return makeCall(url, postBody);
    }

    private okhttp3.Call makeCall(String url, @Nullable Object postBody) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(this.baseUrl + url);

        if (postBody != null) {
            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(postBody));
            requestBuilder.post(requestBody);
        }

        return httpClient.newCall(requestBuilder.build());
    }
}
