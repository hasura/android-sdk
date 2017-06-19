package io.hasura.sdk.model.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class CheckLoginRequest {
    @SerializedName("username")
    String userName;
    @SerializedName("password")
    String password;
    @SerializedName("info")
    JsonObject info;

    public CheckLoginRequest(String userName, String password, JsonObject info) {
        this.userName = userName;
        this.password = password;
        this.info = info;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
