package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {
    @SerializedName("message")
    String message;

    @SerializedName("auth_token")
    String auth_token;

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
      return auth_token;
    }
}
