package io.hasura.sdk.model.request;

import com.google.gson.annotations.SerializedName;

public class ResendOTPRequest {
    @SerializedName("mobile")
    String mobile;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
