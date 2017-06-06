package io.hasura.sdk.auth.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.auth.response.ChangePasswordResponse;
import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraConfig;
import io.hasura.sdk.core.HasuraException;
import io.hasura.sdk.core.HasuraTokenInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 05/06/17.
 */

public class DBService extends HasuraApiService {

    public DBService(HasuraTokenInterceptor tokenInterceptor) {
        super(HasuraConfig.BASE_URL.DB, tokenInterceptor);
    }

    public Builder setRequestBody(JsonObject req) {
        String jsonBody = gson.toJson(req);
        return new Builder(jsonBody);
    }

    public <T> Builder setRequestBody(T t) {
        String jsonBody = gson.toJson(t);
        return new Builder(jsonBody);
    }

    public class Builder {
        String jsonBody;

        Builder(String jsonBody) {
            this.jsonBody = jsonBody;
        }

        public <T> Call<T, HasuraException> build() {
            Type respType = new TypeToken<T>() {}.getType();
            return makePostCall(HasuraConfig.URL.QUERY, jsonBody, respType);
        }
    }

}
