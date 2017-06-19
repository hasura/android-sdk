package io.hasura.sdk.model.request;

import com.google.gson.annotations.SerializedName;

public class ResendEmailRequest {
    @SerializedName("email")
    String email;

    public ResendEmailRequest(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
