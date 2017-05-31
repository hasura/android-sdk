package io.hasura.sdk.auth.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;

import io.hasura.sdk.auth.AuthException;
import io.hasura.sdk.auth.AuthResponseConverter;
import io.hasura.sdk.auth.HasuraTokenInterceptor;
import io.hasura.sdk.core.Call;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class AuthService {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    protected static final Gson gson = new GsonBuilder().create();
    private OkHttpClient httpClient;
    private String authUrl;

    public AuthService(String authUrl, OkHttpClient httpClient) {
        this.authUrl = authUrl;
        this.httpClient = httpClient;
    }

    public AuthService(String authUrl) {
        this.authUrl = authUrl;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HasuraTokenInterceptor())
                .addInterceptor(logging)
                .build();
    }

    public OkHttpClient getClient() {
        return this.httpClient;
    }

    public String getUrl() {
        return this.authUrl;
    }

    protected  <T> Call<T, AuthException> makePostCall(String url, String jsonBody, Type bodyType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.authUrl + url)
                .post(reqBody)
                .build();
        Call<T, AuthException> newCall = new Call<T, AuthException>(httpClient.newCall(request), new AuthResponseConverter<T>(bodyType));
        return newCall;
    }

    protected  <T> Call<T, AuthException> makeGetCall(String url, Type bodyType) {
        Request request = new Request.Builder()
                .url(this.authUrl + url)
                .build();
        Call<T, AuthException> newCall = new Call<T, AuthException>(httpClient.newCall(request), new AuthResponseConverter<T>(bodyType));
        return newCall;
    }

}
