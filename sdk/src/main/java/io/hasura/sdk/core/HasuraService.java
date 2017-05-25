package io.hasura.sdk.core;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import io.hasura.sdk.auth.HasuraException;
import io.hasura.sdk.utils.HasuraTokenInterceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jaison on 23/05/17.
 */

public abstract class HasuraService {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient httpClient;
    private String baseUrl;
    public static final Gson gson = new GsonBuilder().create();

    public HasuraService(String baseUrl, OkHttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public HasuraService(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HasuraTokenInterceptor())
                .addInterceptor(logging)
                .build();
        this.baseUrl = baseUrl;
        this.httpClient = okHttpClient;
    }

    public OkHttpClient getClient() {
        return this.httpClient;
    }

    public String getUrl() {
        return this.baseUrl;
    }

    protected <T> Call<T, HasuraException> mkPostCall(String url, String jsonBody, Type bodyType) {
        return mkCall(url,bodyType, jsonBody);
    }

    protected <T> Call<T, HasuraException> mkPostCall(String url, Object jsonBody) {
        return mkCall(url,jsonBody);
    }

    protected <T> Call<T, HasuraException> mkGetCall(String url, Type bodyType) {
        return mkCall(url, bodyType, null);
    }

    protected <T> Call<T, HasuraException> mkGetCall(String url) {
        return mkCall(url, null);
    }

    private <T> Call<T, HasuraException> mkCall(String url, Type bodyType, @Nullable String jsonBody) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(this.baseUrl + url);
        if (jsonBody != null) {
            RequestBody reqBody = RequestBody.create(JSON, jsonBody);
            requestBuilder.post(reqBody);
        }
        return new Call<>(httpClient.newCall(requestBuilder.build()), new HasuraResponseConverter<T>(bodyType));
    }

    private <T> Call<T, HasuraException> mkCall(String url, @Nullable Object jsonObject) {
        Type respType = new TypeToken<T>() {}.getType();
        return mkCall(url, respType, jsonObject == null ? null : gson.toJson(jsonObject));
    }
}
