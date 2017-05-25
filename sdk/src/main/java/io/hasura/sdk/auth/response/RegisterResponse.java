package io.hasura.sdk.auth.response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("hasura_id")
    int hasuraId;

    @SerializedName("hasura_roles")
    String[] hasuraRoles;

    @SerializedName("auth_token")
    String auth_token;

    public int getHasuraId() {
        return hasuraId;
    }

    public String[] getHasuraRoles() {
        return hasuraRoles;
    }

    public String getSessionId() {
        return auth_token;
    }
}
