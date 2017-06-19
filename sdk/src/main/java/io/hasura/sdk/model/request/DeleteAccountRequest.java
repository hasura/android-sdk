package io.hasura.sdk.model.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DeleteAccountRequest {
    @SerializedName("password")
    String password;
    @SerializedName("info")
    JsonObject info;

    public DeleteAccountRequest(String password) {
        this.password = password;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
