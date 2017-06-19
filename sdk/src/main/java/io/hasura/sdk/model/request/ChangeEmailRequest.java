package io.hasura.sdk.model.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ChangeEmailRequest {
    @SerializedName("email")
    String email;

    @SerializedName("info")
    JsonObject info;

    public ChangeEmailRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }
}
