package io.hasura.sdk.auth.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DeleteAccountRequest {
    @SerializedName("password")
    String password;
    @SerializedName("info")
    JsonObject info;

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
