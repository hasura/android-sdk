package io.hasura.sdk.model.request;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 26/05/17.
 */

public class AuthRequest {

    @SerializedName("username")
    String username;

    @SerializedName("email")
    String email;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("password")
    String password;

    @SerializedName("otp")
    String otp;

    public AuthRequest(String mobile, String otp) {
        this.mobile = mobile;
        this.otp = otp;
    }

    public AuthRequest(String username, @Nullable String email, @Nullable String mobile, String password) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

}
