package io.hasura.sdk.response;

import com.google.gson.annotations.SerializedName;

public class ChangeUserNameResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
