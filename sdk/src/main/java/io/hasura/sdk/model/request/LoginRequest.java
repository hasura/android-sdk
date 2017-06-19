package io.hasura.sdk.model.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("username")
    String userName;

    @SerializedName("password")
    String password;

    @SerializedName("email")
    String email;

    @SerializedName("mobile")
    String mobile;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
