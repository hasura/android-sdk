package io.hasura.sdk.auth;

import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.auth.request.ChangeEmailRequest;
import io.hasura.sdk.auth.request.ChangeMobileRequest;
import io.hasura.sdk.auth.request.ChangePasswordRequest;
import io.hasura.sdk.auth.request.ChangeUserNameRequest;
import io.hasura.sdk.auth.request.CheckPasswordRequest;
import io.hasura.sdk.auth.request.ConfirmEmailRequest;
import io.hasura.sdk.auth.request.ConfirmMobileRequest;
import io.hasura.sdk.auth.request.DeleteAccountRequest;
import io.hasura.sdk.auth.request.ForgotPasswordRequest;
import io.hasura.sdk.auth.request.LoginRequest;
import io.hasura.sdk.auth.request.RegisterRequest;
import io.hasura.sdk.auth.request.ResendEmailRequest;
import io.hasura.sdk.auth.request.ResendOTPRequest;
import io.hasura.sdk.auth.request.ResetPasswordRequest;
import io.hasura.sdk.auth.request.SocialLoginRequest;
import io.hasura.sdk.auth.response.ChangeEmailResponse;
import io.hasura.sdk.auth.response.ChangeMobileResponse;
import io.hasura.sdk.auth.response.ChangePasswordResponse;
import io.hasura.sdk.auth.response.ChangeUserNameResponse;
import io.hasura.sdk.auth.response.CheckPasswordResponse;
import io.hasura.sdk.auth.response.ConfirmEmailResponse;
import io.hasura.sdk.auth.response.ConfirmMobileResponse;
import io.hasura.sdk.auth.response.DeleteAccountResponse;
import io.hasura.sdk.auth.response.ForgotPasswordResponse;
import io.hasura.sdk.auth.response.GetCredentialsResponse;
import io.hasura.sdk.auth.response.LoginResponse;
import io.hasura.sdk.auth.response.LogoutResponse;
import io.hasura.sdk.auth.response.RegisterResponse;
import io.hasura.sdk.auth.response.ResendEmailResponse;
import io.hasura.sdk.auth.response.ResendOTPResponse;
import io.hasura.sdk.auth.response.ResetPasswordResponse;
import io.hasura.sdk.auth.response.SocialLoginResponse;
import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraService;
import io.hasura.sdk.utils.HasuraConfig;

public class AuthService extends HasuraService {

    public AuthService(String authUrl) {
        super(authUrl);
    }

    /**
     * Signup or register a new user
     *
     * @param r a {@link RegisterRequest} type
     * @return  the {@link RegisterResponse}
     * @throws HasuraException
     */
    public Call<RegisterResponse, HasuraException> register(RegisterRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<RegisterResponse>() {}.getType();
        return mkPostCall("/signup", jsonBody, respType);
    }

    public Call<AuthResponse, HasuraException> register(String username, @Nullable String email, @Nullable String mobile, String password) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setMobile(mobile);
        return mkPostCall(HasuraConfig.URL.REGISTER, registerRequest);
    }

    public Call<AuthResponse, HasuraException> registerUsingEmail(String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setUsername(email);
        registerRequest.setPassword(password);
        return mkPostCall(HasuraConfig.URL.REGISTER, registerRequest);
    }

    public Call<AuthResponse, HasuraException> register(String email, String username, String password) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        return mkPostCall(HasuraConfig.URL.REGISTER, registerRequest);
    }

    public Call<AuthResponse, HasuraException> registerUsingUsername(String username, String password) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        return mkPostCall(HasuraConfig.URL.REGISTER, registerRequest);
    }

    public Call<AuthResponse, HasuraException> registerUsingMobileOTP(String mobile) {
        return mkPostCall(HasuraConfig.URL.REGISTER_MOBILE, new MobileAuthRequest(mobile));
    }

    public Call<AuthResponse, HasuraException> registerUsingMobileOTP(String mobile, String username) {
        MobileAuthRequest req = new MobileAuthRequest(mobile);
        req.setUsername(username);
        return mkPostCall(HasuraConfig.URL.REGISTER_MOBILE, req);
    }

    public Call<MessageResponse, HasuraException> loginUsingMobileOTP(String mobile) {
        return mkPostCall(HasuraConfig.URL.LOGIN_MOBILE, new MobileAuthRequest(mobile));
    }

    public Call<AuthResponse, HasuraException> verifyOTPForMobileLogin(String mobile, String otp) {
        return mkPostCall(HasuraConfig.URL.LOGIN_MOBILE, new MobileAuthRequest(mobile, otp));
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
    public Call<LoginResponse, HasuraException> login(LoginRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<LoginResponse>() {}.getType();
        return mkPostCall("/login", jsonBody, respType);
    }

    /**
     * Login an existing user
     *
     * Login an existing user by passing username and password. This is a shortcut for the above
     * method when only username and password is used for login.
     *
     * @param userName the user name of the user
     * @param password password of the user (unencrypted)
     * @return the {@link LoginResponse}
     * @throws HasuraException
     */
    public Call<LoginResponse, HasuraException> login(
            String userName, String password) {
        return this.login(new LoginRequest(userName, password));
    }

    /**
     * Logout a logged-in user.
     *
     * @return a {@link LogoutResponse} type
     * @throws HasuraException
     */
    public Call<LogoutResponse, HasuraException> logout() {
        Type respType = new TypeToken<LogoutResponse>() {
        }.getType();
        return mkGetCall("/user/logout", respType);
    }

    /**
     * Returns credentials of the logged in user
     * <p>
     *     This method can be used to retrieve Hasura credentials for the current logged in user.
     *     Hasura credentials include "Hasura Id", "Hausura Role" and "Session Id". This method can
     *     also be used to check if the user has an existing session (or logged in basically). If
     *     not logged in, it will throw an {@link HasuraException}.
     * </p>
     *
     * @return {@link GetCredentialsResponse}
     * @throws HasuraException
     */
    public Call<GetCredentialsResponse, HasuraException> getCredentials() {
        Type respType = new TypeToken<GetCredentialsResponse>() {
        }.getType();
        return mkGetCall("/user/account/info", respType);
    }

    /**
     * Confirm the email of an user - given an existing token.
     * <p>
     *     Once the user retrieves the token that is sent to the user's email, this method can be
     *     used to confirm the email of the user with Hasura Auth.
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
        return mkGetCall("/email/confirm?token=" + token, respType);
    }

    /**
     * Resend the verification email of an user.
     * <p>
     *     Initialize the {@link ResendEmailRequest} class with the email of the user, and pass the
     *     object to this method.
     * </p>
     *
     * @param r {@link ResendEmailRequest}
     * @return  {@link ResendEmailResponse}
     * @throws HasuraException
     */
    public Call<ResendEmailResponse, HasuraException> resendVerifyEmail(ResendEmailRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResendEmailResponse>() {
        }.getType();
        return mkPostCall("/email/resend-verify", jsonBody, respType);
    }

    /**
     * Change user's email address.
     * <p>
     *     Initialize {@link ChangeEmailRequest} with the new email address of the user. This method
     *     will send a verification email to the new email address of the user.
     * </p>
     *
     * @param r {@link ChangeEmailRequest}
     * @return  {@link ChangeEmailResponse}
     * @throws HasuraException
     */
    public Call<ChangeEmailResponse, HasuraException> changeEmail(ChangeEmailRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangeEmailResponse>() {
        }.getType();
        return mkPostCall("/user/email/change", jsonBody, respType);
    }

    /**
     * Change user's password
     * <p>
     *     This method takes a {@link ChangePasswordRequest} object, which should contain the
     *     current password and the new password.
     * </p>
     *
     * @param r {@link ChangePasswordRequest}
     * @return  {@link ChangePasswordResponse}
     * @throws HasuraException
     */
    public Call<ChangePasswordResponse, HasuraException> changePassword(ChangePasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangePasswordResponse>() {
        }.getType();
        return mkPostCall("/user/password/change", jsonBody, respType);
    }

    /**
     * Send an email to the user, containing the forgot password link.
     *
     * @param r {@link ForgotPasswordRequest}
     * @return  {@link ForgotPasswordResponse}
     * @throws HasuraException
     */
    public Call<ForgotPasswordResponse, HasuraException> forgotPassword(ForgotPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ForgotPasswordResponse>() {
        }.getType();
        return mkPostCall("/password/forgot", jsonBody, respType);
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
    public Call<ResetPasswordResponse, HasuraException> resetPassword(ResetPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResetPasswordResponse>() {
        }.getType();
        return mkPostCall("/password/reset", jsonBody, respType);
    }

    /**
     * Change user's username.
     *
     * @param r {@link ChangeUserNameRequest}
     * @return  {@link ChangeUserNameResponse}
     * @throws HasuraException
     */
    public Call<ChangeUserNameResponse, HasuraException> changeUserName(ChangeUserNameRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangeUserNameResponse>() {
        }.getType();
        return mkPostCall("/user/account/change-username", jsonBody, respType);
    }

    /**
     *
     * @param r
     * @return
     */
    public Call<CheckPasswordResponse, HasuraException> checkPassword(CheckPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<CheckPasswordResponse>() {
        }.getType();
        return mkPostCall("/user/password/verify", jsonBody, respType);
    }

    /**
     * Confirm the mobile number of the user, by passing the OTP and the mobile number of the user.
     *
     * @param r {@link ConfirmMobileRequest}
     * @return  {@link ConfirmMobileResponse}
     * @throws HasuraException
     */
    public Call<ConfirmMobileResponse, HasuraException> confirmMobile(ConfirmMobileRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ConfirmMobileResponse>() {
        }.getType();
        return mkPostCall("/mobile/confirm", jsonBody, respType);
    }

    /**
     * Change user's mobile number. This method will send an OTP to the new number of the user.
     *
     * @param r {@link ChangeMobileRequest}
     * @return  {@link ChangeMobileResponse}
     * @throws HasuraException
     */
    public Call<ChangeMobileResponse, HasuraException> changeMobile(ChangeMobileRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangeMobileResponse>() {
        }.getType();
        return mkPostCall("/user/mobile/change", jsonBody, respType);
    }

    /**
     * Resend the OTP to a user's mobile number.
     *
     * @param r {@link ResendOTPRequest}
     * @return  {@link ResendOTPResponse}
     * @throws HasuraException
     */
    public Call<ResendOTPResponse, HasuraException> resendOTP(ResendOTPRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResendOTPResponse>() {
        }.getType();
        return mkPostCall("/mobile/resend-otp", jsonBody, respType);
    }

    /**
     * Delete account of the current user
     *
     * @param r {@link DeleteAccountRequest}
     * @return  {@link DeleteAccountResponse}
     * @throws HasuraException
     */
    public Call<DeleteAccountResponse, HasuraException> deleteAccount(DeleteAccountRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<DeleteAccountResponse>() {
        }.getType();
        return mkPostCall("/user/account/delete", jsonBody, respType);
    }

    /**
     * Login or create a new user by authenticating with a third-party provider.
     * <p>
     *     After you have obtained "access_token" or an "id_token" from your provider, you have to
     *     pass the token and the provider name to {@link SocialLoginRequest}.
     *     This method will then create the new user (if she is not an existing user), or else it
     *     will login the user.
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
        return mkGetCall(url, respType);
    }
}
