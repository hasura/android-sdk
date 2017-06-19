package io.hasura.sdk.model.request;

import com.google.gson.annotations.SerializedName;

public class ConfirmMobileRequest {
    @SerializedName("mobile")
    String mobile;

    @SerializedName("otp")
    String otp;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOTP(String otp) {
        this.otp = otp;
    }
}
