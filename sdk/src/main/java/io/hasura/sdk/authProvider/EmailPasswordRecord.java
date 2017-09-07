package io.hasura.sdk.authProvider;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 06/09/17.
 */

public class EmailPasswordRecord {

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    public EmailPasswordRecord(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
