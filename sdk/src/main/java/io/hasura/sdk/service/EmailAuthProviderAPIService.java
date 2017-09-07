package io.hasura.sdk.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.hasura.sdk.Call;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.MessageResponse;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 06/09/17.
 */

public class EmailAuthProviderAPIService extends HasuraHttpService {

    public EmailAuthProviderAPIService(String baseUrl, OkHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public Call<MessageResponse, HasuraException> resendEmailVerification(String email) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("email", email);
        String jsonBody = gson.toJson(rootObject);
        return makePostCall("providers/email/resend-verification", jsonBody, new TypeToken<MessageResponse>() {
        }.getType());
    }

    public Call<MessageResponse, HasuraException> forgotPassword(String email) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("email", email);
        String jsonBody = gson.toJson(rootObject);
        return makePostCall("providers/email/forgot-password", jsonBody, new TypeToken<MessageResponse>() {
        }.getType());
    }
}
