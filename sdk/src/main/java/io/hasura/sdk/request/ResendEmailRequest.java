package io.hasura.sdk.request;

import com.google.gson.annotations.SerializedName;

public class ResendEmailRequest {
    @SerializedName("email")
    String email;

    public void setEmail(String email) {
        this.email = email;
    }
}
