package io.hasura.sdk.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.model.request.AuthRequest;
import io.hasura.sdk.model.request.ConfirmEmailRequest;
import io.hasura.sdk.model.request.ConfirmMobileRequest;
import io.hasura.sdk.model.request.ForgotPasswordRequest;
import io.hasura.sdk.model.request.LoginRequest;
import io.hasura.sdk.model.request.ResendEmailRequest;
import io.hasura.sdk.model.request.ResendOTPRequest;
import io.hasura.sdk.model.request.ResetPasswordRequest;
import io.hasura.sdk.model.request.SocialLoginRequest;
import io.hasura.sdk.model.response.AuthResponse;
import io.hasura.sdk.model.response.ConfirmEmailResponse;
import io.hasura.sdk.model.response.ConfirmMobileResponse;
import io.hasura.sdk.model.response.ForgotPasswordResponse;
import io.hasura.sdk.model.response.LoginResponse;
import io.hasura.sdk.model.response.MessageResponse;
import io.hasura.sdk.model.response.ResendOTPResponse;
import io.hasura.sdk.model.response.ResetPasswordResponse;
import io.hasura.sdk.model.response.SocialLoginResponse;
import io.hasura.sdk.Call;
import io.hasura.sdk.exception.HasuraException;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 31/05/17.
 */

public class AnonymousApiService extends HasuraHttpService {

    public AnonymousApiService(String baseUrl, OkHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    /**
     * Signup or register a new user
     *
     * @param r a {@link AuthRequest} type
     * @return  the {@link AuthResponse}
     * @throws HasuraException
     */
    public Call<AuthResponse, HasuraException> signUp(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {
        }.getType();
        return makePostCall("signup", jsonBody, respType);
    }

    public Call<AuthResponse, HasuraException> signUpForMobileOtp(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("otp-signup", jsonBody, respType);
    }

    /**
     * Login an existing user
     *
     * Login an existing user by creating a {@link LoginRequest} class
     *
     * @param r {@link LoginRequest} type
     * @return  the {@link LoginResponse}
     * @throws HasuraException
     */
    public Call<AuthResponse, HasuraException> login(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("login", jsonBody, respType);
    }


    public Call<MessageResponse, HasuraException> sendOtpToMobile(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("otp-login", jsonBody, respType);
    }

    public Call<AuthResponse, HasuraException> otpLogin(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("otp-login", jsonBody, respType);
    }

    /**
     * Login or create a new user by authenticating with a third-party provider.
     * <p>
     *     After you have obtained "access_token" or an "id_token" from your provider, you have to
     *     pass the token and the provider name to {@link SocialLoginRequest}.
     *     This method will then create the new user (if she is not an existing user), or else it
     *     will otpLogin the user.
     * </p>
     * @param r {@link SocialLoginRequest}
     * @return  {@link SocialLoginResponse}
     * @throws HasuraException
     */
    public Call<SocialLoginResponse, HasuraException> socialAuth(SocialLoginRequest r) {
        // the URL is prepared inside the request class
        String url = r.prepareRequestURL();
        Type respType = new TypeToken<SocialLoginResponse>() {
        }.getType();
        return makeGetCall(url, respType);
    }

    /**
     * Confirm the mobile number of the user, by passing the OTP and the mobile number of the user.
     *
     * @return  {@link ConfirmMobileResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> confirmMobile(String mobile, String otp) {
        ConfirmMobileRequest r = new ConfirmMobileRequest();
        r.setMobile(mobile);
        r.setOTP(otp);
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ConfirmMobileResponse>() {
        }.getType();
        return makePostCall("mobile/confirm", jsonBody, respType);
    }


    /**
     * Resend the OTP to a user's mobile number.
     *
     * @param mobile the mobile number to confirm (to which the otp will be sent)
     * @return  {@link ResendOTPResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> resendOTP(String mobile) {
        ResendOTPRequest r = new ResendOTPRequest();
        r.setMobile(mobile);
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResendOTPResponse>() {
        }.getType();
        return makePostCall("mobile/resend-otp", jsonBody, respType);
    }


    /**
     * Confirm the email of an user - given an existing token.
     * <p>
     *     Once the user retrieves the token that is sent to the user's email, this method can be
     *     used to confirm the email of the user with HasuraClient Auth.
     * </p>
     *
     * @param r {@link ConfirmEmailRequest}
     * @return  {@link ConfirmEmailResponse}
     * @throws HasuraException
     */
    public Call<ConfirmEmailResponse, HasuraException> confirmEmail(ConfirmEmailRequest r) {
        String token = r.token;
        Type respType = new TypeToken<ConfirmEmailResponse>() {
        }.getType();
        return makeGetCall("email/confirm?token=" + token, respType);
    }

    /**
     * Resend the verification email of an user.
     * <p>
     *     Initialize the {@link ResendEmailRequest} class with the email of the user, and pass the
     *     object to this method.
     * </p>
     *
     * @param r {@link ResendEmailRequest}
     * @return  {@link MessageResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> resendVerifyEmail(ResendEmailRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<MessageResponse>() {
        }.getType();
        return makePostCall("email/resend-verify", jsonBody, respType);
    }

    /**
     * Send an email to the user, containing the forgot password link.
     *
     * @param r {@link ForgotPasswordRequest}
     * @return  {@link ForgotPasswordResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> forgotPassword(ForgotPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ForgotPasswordResponse>() {
        }.getType();
        return makePostCall("password/forgot", jsonBody, respType);
    }

    /**
     * Reset the password of the user, given the password reset token and the new password.
     * <p>
     *     Initialize the {@link ResetPasswordRequest} object with the password reset token (which
     *     the user retrieves from the forgot password email, and the new password of the user.
     * </p>
     *
     * @param r {@link ResetPasswordRequest}
     * @return  {@link ResetPasswordResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> resetPassword(ResetPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResetPasswordResponse>() {
        }.getType();
        return makePostCall("password/reset", jsonBody, respType);
    }

}
