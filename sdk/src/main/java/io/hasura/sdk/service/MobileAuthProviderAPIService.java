package io.hasura.sdk.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.Call;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.AuthResponse;
import io.hasura.sdk.model.response.MessageResponse;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 06/09/17.
 */

public class MobileAuthProviderAPIService extends HasuraHttpService {

    public MobileAuthProviderAPIService(String baseUrl, OkHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    /**
     * Sends an OTP to the specified mobile number
     * Use the received OTP for Mobile-otp signup
     *
     * @param mobileNumber - the mobile number to which the OTP will be sent
     * @return
     */
    public Call<MessageResponse, HasuraException> sendOtp(String mobileNumber) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("mobile", mobileNumber);
        String jsonBody = gson.toJson(rootObject);
        Type respType = new TypeToken<AuthResponse>() {
        }.getType();
        return makePostCall("providers/mobile/send-otp", jsonBody, respType);
    }

    /**
     * re-sends otp to the specified mobile number
     *
     * @param mobileNumber - the mobile number to which the OTP will be sent
     * @return
     */
    public Call<MessageResponse, HasuraException> resendOtp(String mobileNumber) {
        JsonObject rootObject = new JsonObject();
        rootObject.addProperty("mobile", mobileNumber);
        String jsonBody = gson.toJson(rootObject);
        Type respType = new TypeToken<AuthResponse>() {
        }.getType();
        return makePostCall("providers/mobile/resend-otp", jsonBody, respType);
    }
}
