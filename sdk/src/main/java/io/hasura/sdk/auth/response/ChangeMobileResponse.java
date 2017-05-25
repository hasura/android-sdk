package io.hasura.sdk.auth.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ChangeMobileResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
