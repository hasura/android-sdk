package io.hasura.sdk.response;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}

