package io.hasura.sdk.auth.request;

import com.google.gson.annotations.SerializedName;

public class ConfirmEmailRequest {
    @SerializedName("token")
    public String token;

    public void setToken(String token) {
        this.token = token;
    }
}
