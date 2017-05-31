package io.hasura.sdk.auth;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.auth.request.AuthRequest;
import io.hasura.sdk.auth.request.ConfirmEmailRequest;
import io.hasura.sdk.auth.request.ConfirmMobileRequest;
import io.hasura.sdk.auth.request.ForgotPasswordRequest;
import io.hasura.sdk.auth.request.LoginRequest;
import io.hasura.sdk.auth.request.ResendEmailRequest;
import io.hasura.sdk.auth.request.ResendOTPRequest;
import io.hasura.sdk.auth.request.ResetPasswordRequest;
import io.hasura.sdk.auth.request.SocialLoginRequest;
import io.hasura.sdk.auth.response.AuthResponse;
import io.hasura.sdk.auth.response.ConfirmEmailResponse;
import io.hasura.sdk.auth.response.ConfirmMobileResponse;
import io.hasura.sdk.auth.response.ForgotPasswordResponse;
import io.hasura.sdk.auth.response.LoginResponse;
import io.hasura.sdk.auth.response.MessageResponse;
import io.hasura.sdk.auth.response.ResendOTPResponse;
import io.hasura.sdk.auth.response.ResetPasswordResponse;
import io.hasura.sdk.auth.response.SocialLoginResponse;
import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.HasuraConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jaison on 31/05/17.
 */

public class AnonymousUserService extends AuthService {

    private static AnonymousUserService instance;

    public static AnonymousUserService getInstance() {
        if (instance == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient c = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
            instance = new AnonymousUserService(HasuraConfig.BASE_URL.AUTH);
        }

        return instance;
    }

    private AnonymousUserService(String authUrl, OkHttpClient httpClient) {
        super(authUrl, httpClient);
    }

    private AnonymousUserService(String authUrl) {
        super(authUrl);
    }

    /**
     * Signup or register a new user
     *
     * @param r a {@link AuthRequest} type
     * @return  the {@link AuthResponse}
     * @throws AuthException
     */
    public Call<AuthResponse, AuthException> signUp(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {
        }.getType();
        return makePostCall("/signup", jsonBody, respType);
    }

    public Call<AuthResponse, AuthException> signUpForMobileOtp(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("/otp-signup", jsonBody, respType);
    }

    /**
     * Login an existing user
     *
     * Login an existing user by creating a {@link LoginRequest} class
     *
     * @param r {@link LoginRequest} type
     * @return  the {@link LoginResponse}
     * @throws AuthException
     */
    public Call<AuthResponse, AuthException> login(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<LoginResponse>() {}.getType();
        return makePostCall("/login", jsonBody, respType);
    }


    public Call<MessageResponse, AuthException> sendOtpToMobile(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("/otp-login", jsonBody, respType);
    }

    public Call<AuthResponse, AuthException> otpLogin(AuthRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<AuthResponse>() {}.getType();
        return makePostCall("/otp-login", jsonBody, respType);
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
     * @throws AuthException
     */
    public Call<AuthResponse, AuthException> login(
            String userName, String password) {
        return this.login(new AuthRequest(userName, password));
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
     * @throws AuthException
     */
    public Call<ConfirmEmailResponse, AuthException> confirmEmail(ConfirmEmailRequest r) {
        String token = r.token;
        Type respType = new TypeToken<ConfirmEmailResponse>() {
        }.getType();
        return makeGetCall("/email/confirm?token=" + token, respType);
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
     * @throws AuthException
     */
    public Call<MessageResponse, AuthException> resendVerifyEmail(ResendEmailRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<MessageResponse>() {
        }.getType();
        return makePostCall("/email/resend-verify", jsonBody, respType);
    }

    /**
     * Send an email to the user, containing the forgot password link.
     *
     * @param r {@link ForgotPasswordRequest}
     * @return  {@link ForgotPasswordResponse}
     * @throws AuthException
     */
    public Call<MessageResponse, AuthException> forgotPassword(ForgotPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ForgotPasswordResponse>() {
        }.getType();
        return makePostCall("/password/forgot", jsonBody, respType);
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
     * @throws AuthException
     */
    public Call<MessageResponse, AuthException> resetPassword(ResetPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResetPasswordResponse>() {
        }.getType();
        return makePostCall("/password/reset", jsonBody, respType);
    }


    /**
     * Confirm the mobile number of the user, by passing the OTP and the mobile number of the user.
     *
     * @return  {@link ConfirmMobileResponse}
     * @throws AuthException
     */
    public Call<MessageResponse, AuthException> confirmMobile(String mobile, String otp) {
        ConfirmMobileRequest r = new ConfirmMobileRequest();
        r.setMobile(mobile);
        r.setOTP(otp);
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ConfirmMobileResponse>() {
        }.getType();
        return makePostCall("/mobile/confirm", jsonBody, respType);
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
     * @throws AuthException
     */
    public Call<AuthResponse, AuthException> socialAuth(SocialLoginRequest r) {
        // the URL is prepared inside the request class
        String url = r.prepareRequestURL();
        Type respType = new TypeToken<SocialLoginResponse>() {
        }.getType();
        return makeGetCall(url, respType);
    }


    /**
     * Resend the OTP to a user's mobile number.
     *
     * @param r {@link ResendOTPRequest}
     * @return  {@link ResendOTPResponse}
     * @throws AuthException
     */
    public Call<MessageResponse, AuthException> resendOTP(ResendOTPRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ResendOTPResponse>() {
        }.getType();
        return makePostCall("/mobile/resend-otp", jsonBody, respType);
    }
}
