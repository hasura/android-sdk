package io.hasura.sdk.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ChangeMobileRequest {
    @SerializedName("mobile")
    String mobile;

    @SerializedName("info")
    JsonObject info;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }
}
