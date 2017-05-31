package io.hasura.sdk.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 31/05/17.
 */

public class MessageResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
