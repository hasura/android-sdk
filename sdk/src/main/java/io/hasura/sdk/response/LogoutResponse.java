package io.hasura.sdk.response;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public Boolean isSuccess() {
        return message == "Logged out";
    }
}
