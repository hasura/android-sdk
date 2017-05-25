package io.hasura.sdk.auth.request;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest {
    @SerializedName("password")
    String password;
    @SerializedName("new_password")
    String newPassword;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
