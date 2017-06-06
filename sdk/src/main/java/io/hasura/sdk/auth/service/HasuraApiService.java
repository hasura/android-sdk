package io.hasura.sdk.auth.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraConfig;
import io.hasura.sdk.core.HasuraException;
import io.hasura.sdk.core.HasuraResponseConverter;
import io.hasura.sdk.core.HasuraTokenInterceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class HasuraApiService {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final Gson gson = new GsonBuilder().create();
    private OkHttpClient httpClient;
    private String baseUrl;

    public HasuraApiService(String baseUrl, OkHttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public HasuraApiService(String baseUrl, HasuraTokenInterceptor tokenInterceptor) {
        this.baseUrl = baseUrl;
        this.httpClient = getHttpClient(tokenInterceptor);
    }

    public HasuraApiService(String baseUrl) {
        this.baseUrl = baseUrl;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.httpClient = getHttpClient();
    }

    private static OkHttpClient getHttpClient(HasuraTokenInterceptor tokenInterceptor) {
        OkHttpClient.Builder builder  = new OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor);
        if (HasuraConfig.SDK.isLoggingEnabled) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder  = new OkHttpClient.Builder();
        if (HasuraConfig.SDK.isLoggingEnabled) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }

    protected  <T> Call<T, HasuraException> makePostCall(String url, String jsonBody, Type responseType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .post(reqBody)
                .build();
        Call<T, HasuraException> newCall = new Call<T, HasuraException>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
        return newCall;
    }

    protected  <T> Call<T, HasuraException> makeGetCall(String url, Type responseType) {
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .build();
        Call<T, HasuraException> newCall = new Call<T, HasuraException>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
        return newCall;
    }

    protected  <T> Call<T, HasuraException> makePutCall(String url, String jsonBody, Type responseType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .put(reqBody)
                .build();
        Call<T, HasuraException> newCall = new Call<T, HasuraException>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
        return newCall;
    }

    protected  <T> Call<T, HasuraException> makeDeleteCall(String url, String jsonBody, Type responseType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .delete(reqBody)
                .build();
        Call<T, HasuraException> newCall = new Call<T, HasuraException>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
        return newCall;
    }

    protected  <T> Call<T, HasuraException> makeDeleteCall(String url, Type responseType) {
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .delete()
                .build();
        Call<T, HasuraException> newCall = new Call<T, HasuraException>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
        return newCall;
    }

}
