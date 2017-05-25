package io.hasura.sdk.auth.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {
    @SerializedName("message")
    String message;

    @SerializedName("auth_token")
    String auth_token;

    public String getMessage() {
        return message;
    }

    public String getSessionId() {
      return auth_token;
    }
}
