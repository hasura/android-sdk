package io.hasura.sdk.auth.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraConfig;
import io.hasura.sdk.core.HasuraException;
import io.hasura.sdk.core.HasuraTokenInterceptor;

/**
 * Created by jaison on 06/06/17.
 */

public class QueryTemplateService extends HasuraApiService {

    public QueryTemplateService(HasuraTokenInterceptor tokenInterceptor) {
        super(HasuraConfig.BASE_URL.DB, tokenInterceptor);
    }

    public Builder getBuilder(String templateName) {
        return new Builder(templateName);
    }

    public class Builder {
        String templateName;
        JsonObject requestBody;

        private String getPathURL() {
            return HasuraConfig.URL.QUERY_TEMPLATE + "/" + templateName;
        }

        Builder(String templateName) {
            this.templateName = templateName;
        }

        public Builder setParams(String key, String value) {
            if (requestBody == null) {
                requestBody = new JsonObject();
            }
            requestBody.addProperty(key, value);
            return this;
        }

        public <T> Call<T, HasuraException> build() {
            Type respType = new TypeToken<T>() {}.getType();
            if (requestBody == null) {
                return makeGetCall(getPathURL(), respType);
            }
            String jsonBody = gson.toJson(requestBody);
            return makePutCall(getPathURL(), jsonBody, respType);
        }
    }
}
