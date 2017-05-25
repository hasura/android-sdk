package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class MessageResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }
}
