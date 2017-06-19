package io.hasura.sdk.query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import io.hasura.sdk.Call;
import io.hasura.sdk.Converter;
import io.hasura.sdk.HasuraListResponseConverter;
import io.hasura.sdk.HasuraResponseConverter;
import io.hasura.sdk.HasuraTokenInterceptor;
import io.hasura.sdk.exception.HasuraException;
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

    private HasuraQuery(okhttp3.Call rawCall, Converter<T, HasuraException> converter) {
        super(rawCall, converter);
    }

    public static final class Builder {
        String baseUrl;
        JsonObject requestbodyJSON;
        OkHttpClient httpClient;

        public Builder(OkHttpClient client) {
            this.httpClient = client;
        }


        private JsonObject getRequestBody() {
            if (requestbodyJSON == null) {
                requestbodyJSON = new JsonObject();
            }
            return requestbodyJSON;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
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
