package io.hasura.sdk.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import io.hasura.sdk.Call;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseConverter.HasuraResponseConverter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HasuraHttpService {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final Gson gson = new GsonBuilder().create();
    private OkHttpClient httpClient;
    private String baseUrl;

    public HasuraHttpService(String baseUrl, OkHttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    protected <T> Call<T, HasuraException> makePostCall(String url, String jsonBody, Type responseType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .post(reqBody)
                .build();
        return new Call<>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
    }

    protected <T> Call<T, HasuraException> makeGetCall(String url, Type responseType) {
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .build();
        return new Call<>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
    }

    protected <T> Call<T, HasuraException> makePutCall(String url, String jsonBody, Type responseType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .put(reqBody)
                .build();
        return new Call<>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
    }

    protected <T> Call<T, HasuraException> makeDeleteCall(String url, String jsonBody, Type responseType) {
        RequestBody reqBody = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .delete(reqBody)
                .build();
        return new Call<>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
    }

    protected <T> Call<T, HasuraException> makeDeleteCall(String url, Type responseType) {
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .delete()
                .build();
        return new Call<>(httpClient.newCall(request), new HasuraResponseConverter<T>(responseType));
    }

}
