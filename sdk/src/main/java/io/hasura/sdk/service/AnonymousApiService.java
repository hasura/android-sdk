package io.hasura.sdk.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.Call;
import io.hasura.sdk.authProvider.HasuraAuthProvider;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.AuthResponse;
import io.hasura.sdk.model.response.MessageResponse;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 31/05/17.
 */

public class AnonymousApiService extends HasuraHttpService {

    public AnonymousApiService(String baseUrl, OkHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    public Call<AuthResponse, HasuraException> signUp(HasuraAuthProvider authProvider) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("provider", authProvider.getProviderType());
        JsonElement dataElement = new Gson().toJsonTree(authProvider.getDataObject());
        rootObject.add("data", dataElement);
        String jsonBody = gson.toJson(rootObject);
        return makePostCall("signup", jsonBody, new TypeToken<AuthResponse>() {
        }.getType());
    }

    public Call<AuthResponse, HasuraException> login(HasuraAuthProvider authProvider) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("provider", authProvider.getProviderType());
        JsonElement dataElement = new Gson().toJsonTree(authProvider.getDataObject());
        rootObject.add("data", dataElement);
        String jsonBody = gson.toJson(rootObject);
        return makePostCall("login", jsonBody, new TypeToken<AuthResponse>() {
        }.getType());
    }
}
