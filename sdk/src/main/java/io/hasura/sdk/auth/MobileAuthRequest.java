package io.hasura.sdk.auth;

import com.google.gson.annotations.SerializedName;

public class MobileAuthRequest {

    @SerializedName("username")
    String username;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("otp")
    String otp;

    @SerializedName("password")
    String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public MobileAuthRequest(String mobile) {
        this.mobile = mobile;
        this.username = mobile;
    }

    public MobileAuthRequest(String mobile, String otp) {
        this.username = mobile;
        this.mobile = mobile;
        this.otp = otp;
    }

}