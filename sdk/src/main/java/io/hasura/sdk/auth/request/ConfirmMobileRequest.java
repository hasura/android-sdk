package io.hasura.sdk.auth.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ConfirmMobileRequest {
    @SerializedName("mobile")
    String mobile;

    @SerializedName("otp")
    int otp;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOTP(int otp) {
        this.otp = otp;
    }
}
