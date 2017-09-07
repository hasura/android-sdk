package io.hasura.sdk.authProvider;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 06/09/17.
 */

public class UsernamePasswordRecord {
    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    public UsernamePasswordRecord(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
