package io.hasura.sdk.model.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("hasura_id")
    int hasuraId;

    @SerializedName("hasura_roles")
    String[] hasuraRoles;

    @SerializedName("auth_token")
    String auth_token;

     @SerializedName("info")
    JsonObject info;

    public int getHasuraId() {
        return hasuraId;
    }

    public String[] getHasuraRoles() {
        return hasuraRoles;
    }

    public String getSessionId() {
        return auth_token;
    }

    public JsonObject getInfo() {
        return info;
    }
}
