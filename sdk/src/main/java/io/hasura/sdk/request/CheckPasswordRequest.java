package io.hasura.sdk.request;

import com.google.gson.annotations.SerializedName;

public class CheckPasswordRequest {
    @SerializedName("password")
    String password;

    public void setPassword(String password) {
        this.password = password;
    }
}
