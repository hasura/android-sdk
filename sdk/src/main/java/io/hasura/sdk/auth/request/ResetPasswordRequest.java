package io.hasura.sdk.auth.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    @SerializedName("token")
    String token;

    @SerializedName("password")
    String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
