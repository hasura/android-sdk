package io.hasura.sdk.auth.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraConfig;
import io.hasura.sdk.core.HasuraException;
import io.hasura.sdk.core.HasuraTokenInterceptor;

/**
 * Created by jaison on 05/06/17.
 */

public class CustomService extends HasuraApiService {

    public CustomService(String serviceName, HasuraTokenInterceptor tokenInterceptor) {
        super(HasuraConfig.getCustomServiceURL(serviceName), tokenInterceptor);
    }

    public GETRequestBuilder GET(String path) {
        return new GETRequestBuilder(path);
    }

    public POSTRequestBuilder POST(String path) {
        return new POSTRequestBuilder(path);
    }

    public PUTRequestBuilder PUT(String path) {
        return new PUTRequestBuilder(path);
    }

    public DELETERequestBuilder DELETE(String path) {
        return new DELETERequestBuilder(path);
    }

    public class PUTRequestBuilder {

        String path;
        JsonObject requestBody;

        public PUTRequestBuilder(String path) {
            this.path = path;
        }

        public PUTRequestBuilder setParams(String key, String value) {
            if (requestBody == null) {
                requestBody = new JsonObject();
            }
            requestBody.addProperty(key, value);
            return this;
        }

        public <T> Call<T, HasuraException> build() {
            Type respType = new TypeToken<T>() {
            }.getType();
            String jsonBody = gson.toJson(requestBody);
            return makePutCall(path, jsonBody, respType);
        }
    }

    public class DELETERequestBuilder {

        String path;
        JsonObject requestBody;

        public DELETERequestBuilder(String path) {
            this.path = path;
        }

        public DELETERequestBuilder setParams(String key, String value) {
            if (requestBody == null) {
                requestBody = new JsonObject();
            }
            requestBody.addProperty(key, value);
            return this;
        }

        public <T> Call<T, HasuraException> build() {
            Type respType = new TypeToken<T>() {}.getType();
            if (requestBody == null) {
                return makeDeleteCall(path, respType);
            }
            String jsonBody = gson.toJson(requestBody);
            return makeDeleteCall(path, jsonBody, respType);
        }
    }

    public class POSTRequestBuilder {

        String path;
        JsonObject requestBody;

        public POSTRequestBuilder(String path) {
            this.path = path;
        }

        public POSTRequestBuilder setParams(String key, String value) {
            if (requestBody == null) {
                requestBody = new JsonObject();
            }
            requestBody.addProperty(key, value);
            return this;
        }

        public <T> Call<T, HasuraException> build() {
            Type respType = new TypeToken<T>() {
            }.getType();
            String jsonBody = gson.toJson(requestBody);
            return makePostCall(path, jsonBody, respType);
        }
    }

    public class GETRequestBuilder {

        String path;

        public GETRequestBuilder(String path) {
            this.path = path;
        }

        public <T> Call<T, HasuraException> build() {
            Type respType = new TypeToken<T>() {
            }.getType();
            return makeGetCall(path, respType);
        }

    }

}
