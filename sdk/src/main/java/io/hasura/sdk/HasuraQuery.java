package io.hasura.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.response.AuthResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jaison on 09/06/17.
 */

public class HasuraQuery<T> extends Call<T, HasuraException> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final Gson gson = new GsonBuilder().create();

    public HasuraQuery(okhttp3.Call rawCall, Converter<T, HasuraException> converter) {
        super(rawCall, converter);
    }

    public static final class Builder {
        String baseUrl;
        JsonObject requestbodyJSON;
        OkHttpClient httpClient;

        public Builder(HasuraTokenInterceptor tokenInterceptor) {
            this.httpClient = OkHttpClientProvider.getClientForTokenInterceptor(tokenInterceptor);
        }

        public Builder() {
            this.httpClient = OkHttpClientProvider.getHttpClient();
        }

        private JsonObject getRequestBody() {
            if (requestbodyJSON == null) {
                requestbodyJSON = new JsonObject();
            }
            return requestbodyJSON;
        }

        public Builder useDataService() {
            this.baseUrl = HasuraConfig.BASE_URL.DB + HasuraConfig.URL.QUERY;
            return this;
        }

        public Builder useQueryTemplate(String templateName) {
            this.baseUrl = HasuraConfig.BASE_URL.DB + HasuraConfig.URL.QUERY_TEMPLATE + "/" + templateName;
            return this;
        }

        public Builder setRequestParams(String key, String value) {
            getRequestBody().addProperty(key, value);
            return this;
        }

        public Builder setRequestParams(String key, Number value) {
            getRequestBody().addProperty(key, value);
            return this;
        }

        public Builder setRequestParams(String key, Boolean value) {
            getRequestBody().addProperty(key, value);
            return this;
        }

        public Builder setRequestParams(String key, Character value) {
            getRequestBody().addProperty(key, value);
            return this;
        }

        public Builder setRequestBody(JsonObject requestBody) {
            this.requestbodyJSON = requestBody;
            return this;
        }

        public <U> Builder setRequestBody(U t) throws JsonSyntaxException {
            String jsonString = gson.toJson(t);
            this.requestbodyJSON = gson.fromJson(jsonString, JsonObject.class);
            return this;
        }

        public <T> HasuraQuery<List<T>> expectResponseTypeArrayOf(Class<T> clazz) {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(baseUrl);
            if (requestbodyJSON != null) {
                String jsonBody = gson.toJson(requestbodyJSON);
                RequestBody reqBody = RequestBody.create(JSON, jsonBody);
                requestBuilder.post(reqBody);
            }
            return new HasuraQuery<>(httpClient.newCall(requestBuilder.build()), new HasuraListResponseConverter<>(clazz));

        }

        public <T> HasuraQuery<T> expectResponseType(Class<T> clazz) {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(baseUrl);
            if (requestbodyJSON != null) {
                String jsonBody = gson.toJson(requestbodyJSON);
                RequestBody reqBody = RequestBody.create(JSON, jsonBody);
                requestBuilder.post(reqBody);
            }
            return new HasuraQuery<>(httpClient.newCall(requestBuilder.build()), new HasuraResponseConverter<>(clazz));
        }

    }


}
