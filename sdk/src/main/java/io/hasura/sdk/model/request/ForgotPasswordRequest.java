package io.hasura.sdk.model.request;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {
    @SerializedName("email")
    String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
