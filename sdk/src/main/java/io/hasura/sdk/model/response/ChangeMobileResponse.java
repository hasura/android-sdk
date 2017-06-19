package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

public class ChangeMobileResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
