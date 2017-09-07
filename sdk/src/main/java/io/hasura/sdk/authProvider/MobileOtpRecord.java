package io.hasura.sdk.authProvider;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 06/09/17.
 */

public class MobileOtpRecord {
    @SerializedName("mobile")
    String mobile;

    @SerializedName("otp")
    String otp;

    public MobileOtpRecord(String mobile, String otp) {
        this.mobile = mobile;
        this.otp = otp;
    }
}
