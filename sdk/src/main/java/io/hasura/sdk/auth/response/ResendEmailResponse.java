package io.hasura.sdk.auth.response;

import com.google.gson.annotations.SerializedName;

public class ResendEmailResponse {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
