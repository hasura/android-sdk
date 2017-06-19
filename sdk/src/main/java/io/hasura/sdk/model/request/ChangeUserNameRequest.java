package io.hasura.sdk.model.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ChangeUserNameRequest {
    @SerializedName("username")
    String username;
    @SerializedName("info")
    JsonObject info;

    public ChangeUserNameRequest(String username) {
        this.username = username;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
