package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
