package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class AuthRequest {

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
