package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
