package io.hasura.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.request.AuthRequest;
import io.hasura.sdk.model.request.ChangeEmailRequest;
import io.hasura.sdk.model.request.ChangeMobileRequest;
import io.hasura.sdk.model.request.ChangePasswordRequest;
import io.hasura.sdk.model.request.ChangeUserNameRequest;
import io.hasura.sdk.model.request.DeleteAccountRequest;
import io.hasura.sdk.model.request.ResendEmailRequest;
import io.hasura.sdk.model.request.SocialLoginRequest;
import io.hasura.sdk.model.response.AuthResponse;
import io.hasura.sdk.model.response.ChangePasswordResponse;
import io.hasura.sdk.model.response.GetCredentialsResponse;
import io.hasura.sdk.model.response.LogoutResponse;
import io.hasura.sdk.model.response.MessageResponse;
import io.hasura.sdk.model.response.SocialLoginResponse;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.ChangeEmailResponseListener;
import io.hasura.sdk.responseListener.ChangeMobileResponseListener;
import io.hasura.sdk.responseListener.ChangePasswordResponseListener;
import io.hasura.sdk.responseListener.ChangeUserNameResponseListener;
import io.hasura.sdk.responseListener.DeleteAccountResponseListener;
import io.hasura.sdk.responseListener.EmailVerificationSenderListener;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.sdk.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;
import io.hasura.sdk.responseListener.SuccessFailureResponseListener;
import io.hasura.sdk.responseListener.SyncStatusListener;
import io.hasura.sdk.service.AnonymousApiService;
import io.hasura.sdk.service.AnonymousUserApi;
import io.hasura.sdk.service.AuthenticatedUserApi;
import io.hasura.sdk.service.UserApiService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jaison on 30/05/17.
 */

public class HasuraUser implements AnonymousUserApi, AuthenticatedUserApi {

    interface StateChangeListener {
        void onAuthTokenChanged(String authToken);

        void onRolesChanged(String[] roles);

        void onSocialLoginAccessTokenChanged(Map<HasuraSocialLoginType, String> map);
    }

    private Integer id;
    private String email;
    private String username;
    private String mobile;
    private String[] roles;
    private String authToken;
    private String password;
    private Map<HasuraSocialLoginType, String> socialLoginTypeAccessTokenMap = new HashMap<>();

    private AnonymousApiService anonApiService;
    private UserApiService userApiService;
    private StateChangeListener stateChangeListener;

    private class UserApiInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            if (authToken != null) {
                builder.addHeader("Authorization", "Bearer " + authToken);
            }
            builder.addHeader("X-HasuraClient-Role", "user");
            return chain.proceed(builder.build());
        }
    }

    public static final class Builder {

        private String authUrl;
        private Boolean isLoggingEnabled;
        private StateChangeListener stateChangeListener;

        public Builder setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
            return this;
        }

        public Builder shouldEnableLogging(Boolean shouldEnableLogin) {
            this.isLoggingEnabled = shouldEnableLogin;
            return this;
        }

        public Builder setStateChangeListener(StateChangeListener listener) {
            this.stateChangeListener = listener;
            return this;
        }

        public HasuraUser build() {
            return new HasuraUser(authUrl, isLoggingEnabled, stateChangeListener);
        }

    }

    private HasuraUser(String authUrl, Boolean shouldEnableLogging, StateChangeListener listener) {
        OkHttpClient.Builder anonClientBuilder = new OkHttpClient.Builder();
        OkHttpClient.Builder authClientBuilder = new OkHttpClient.Builder();

        if (shouldEnableLogging) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            anonClientBuilder.addInterceptor(logging);
            authClientBuilder.addInterceptor(logging);
        }

        authClientBuilder.addInterceptor(new UserApiInterceptor());
        this.anonApiService = new AnonymousApiService(authUrl, anonClientBuilder.build());
        this.userApiService = new UserApiService(authUrl, authClientBuilder.build());
        this.stateChangeListener = listener;
    }

    private HasuraUser() {
    }

    private HasuraUser(HasuraUser user) {
        setId(user.getId());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        setMobile(user.getMobile());
        setRoles(user.getRoles());
        setAuthToken(user.getAuthToken());
        setPassword(user.getPassword());
    }

    public Boolean isLoggedIn() {
        return authToken != null;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public String[] getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccesstokenForSocialLogin(HasuraSocialLoginType type, String accessToken) {
        socialLoginTypeAccessTokenMap.put(type, accessToken);
        this.stateChangeListener.onSocialLoginAccessTokenChanged(socialLoginTypeAccessTokenMap);
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
        this.stateChangeListener.onRolesChanged(roles);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        this.stateChangeListener.onAuthTokenChanged(authToken);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private AuthRequest getAuthRequest() {
        return new AuthRequest(this.username, this.email, this.mobile, this.password);
    }

    private class SignUpResponseCallbackHandler implements Callback<AuthResponse, HasuraException> {

        SignUpResponseListener listener;

        SignUpResponseCallbackHandler(SignUpResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(AuthResponse response) {
            setId(response.getId());
            setRoles(response.getRoles());
            setAuthToken(response.getAuthToken());
            HasuraSessionStore.saveUser(HasuraUser.this);

            if (response.getAuthToken() == null) {
                if (listener != null) {
                    listener.onSuccessAwaitingVerification(HasuraUser.this);
                }
            } else {
                if (listener != null) {
                    listener.onSuccess(HasuraUser.this);
                }
            }
        }

        @Override
        public void onFailure(HasuraException e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    private class AuthResponseCallbackHandler implements Callback<AuthResponse, HasuraException> {

        AuthResponseListener listener;

        AuthResponseCallbackHandler(AuthResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(AuthResponse response) {
            setId(response.getId());
            setRoles(response.getRoles());
            setAuthToken(response.getAuthToken());

            HasuraSessionStore.saveUser(HasuraUser.this);

            if (listener != null) {
                listener.onSuccess("Login Successful");
            }
        }

        @Override
        public void onFailure(HasuraException e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    private class SocialAuthResponseCallbackHandler implements Callback<SocialLoginResponse, HasuraException> {

        AuthResponseListener listener;
        HasuraSocialLoginType type;

        SocialAuthResponseCallbackHandler(HasuraSocialLoginType type, AuthResponseListener listener) {
            this.listener = listener;
            this.type = type;
        }

        @Override
        public void onSuccess(SocialLoginResponse response) {
            setId(response.getId());
            setRoles(response.getRoles());
            setAuthToken(response.getAuthToken());

            HasuraSessionStore.saveUser(HasuraUser.this);

            if (listener != null) {
                listener.onSuccess("Login Successful");
            }
        }

        @Override
        public void onFailure(HasuraException e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    private class SuccessFailureCallbackHandler implements Callback<MessageResponse, HasuraException> {

        SuccessFailureResponseListener listener;

        public SuccessFailureCallbackHandler(SuccessFailureResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(MessageResponse response) {
            if (listener != null) {
                listener.onSuccess(response.getMessage());
            }
        }

        @Override
        public void onFailure(HasuraException e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    @Override
    public void otpSignUp(final SignUpResponseListener listener) {
        anonApiService.signUpForMobileOtp(getAuthRequest())
                .enqueue(new SignUpResponseCallbackHandler(listener));
    }

    @Override
    public void signUp(final SignUpResponseListener listener) {
        anonApiService.signUp(getAuthRequest())
                .enqueue(new SignUpResponseCallbackHandler(listener));
    }

    @Override
    public void sendOtpToMobile(final OtpStatusListener listener) {
        anonApiService.sendOtpToMobile(getAuthRequest())
                .enqueue(new SuccessFailureCallbackHandler(listener));
    }

    @Override
    public void resendOTP(final OtpStatusListener listener) {
        anonApiService.resendOTP(mobile)
                .enqueue(new SuccessFailureCallbackHandler(listener));
    }

    @Override
    public void resendVerificationEmail(final EmailVerificationSenderListener listener) {
        anonApiService.resendVerifyEmail(new ResendEmailRequest(email))
                .enqueue(new SuccessFailureCallbackHandler(listener));
    }

    @Override
    public void otpLogin(String otp, final AuthResponseListener listener) {
        AuthRequest request = new AuthRequest(mobile, otp);
        anonApiService.otpLogin(request)
                .enqueue(new AuthResponseCallbackHandler(listener));
    }

    @Override
    public void login(final AuthResponseListener listener) {
        anonApiService.login(getAuthRequest())
                .enqueue(new AuthResponseCallbackHandler(listener));
    }

    @Override
    public void socialLogin(HasuraSocialLoginType type, String token, final AuthResponseListener listener) {
        anonApiService.socialAuth(new SocialLoginRequest(type.getCode(), token))
                .enqueue(new SocialAuthResponseCallbackHandler(type, listener));
    }

    @Override
    public void confirmMobile(String otp, final MobileConfirmationResponseListener listener) {
        anonApiService.confirmMobile(mobile, otp)
                .enqueue(new SuccessFailureCallbackHandler(listener));
    }

    @Override
    public void confirmMobileAndLogin(String otp, final AuthResponseListener listener) {
        confirmMobile(otp, new MobileConfirmationResponseListener() {
            @Override
            public void onSuccess(String message) {
                login(listener);
            }

            @Override
            public void onFailure(HasuraException e) {
                if (listener != null) {
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void resendOtpForMobileConfirmation(final OtpStatusListener listener) {
        anonApiService.resendOTP(mobile)
                .enqueue(new SuccessFailureCallbackHandler(listener));
    }


    @Override
    public void sync(final SyncStatusListener listener) {
        userApiService.getCredentials()
                .enqueue(new Callback<GetCredentialsResponse, HasuraException>() {
                    @Override
                    public void onSuccess(GetCredentialsResponse response) {
                        setId(response.getHasuraId());
                        setAuthToken(response.getSessionId());
                        setRoles(response.getHasuraRoles());
                        HasuraSessionStore.saveUser(HasuraUser.this);
                        if (listener != null) {
                            listener.onSuccess("Sync Successful");
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    @Override
    public void changePassword(String newPassword, final ChangePasswordResponseListener listener) {
        userApiService.changePassword(new ChangePasswordRequest(password, newPassword))
                .enqueue(new Callback<ChangePasswordResponse, HasuraException>() {
                    @Override
                    public void onSuccess(ChangePasswordResponse response) {
                        setAuthToken(response.getAuthToken());
                        HasuraSessionStore.saveUser(HasuraUser.this);
                        if (listener != null) {
                            listener.onSuccess(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    @Override
    public void changeEmail(final String newEmail, final ChangeEmailResponseListener listener) {
        userApiService.changeEmail(new ChangeEmailRequest(newEmail))
                .enqueue(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        setEmail(newEmail);
                        HasuraSessionStore.saveUser(HasuraUser.this);
                        if (listener != null) {
                            listener.onSuccess(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    @Override
    public void changeMobile(final String newMobile, final ChangeMobileResponseListener listener) {
        userApiService.changeMobile(new ChangeMobileRequest(newMobile))
                .enqueue(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        setMobile(newMobile);
                        HasuraSessionStore.saveUser(HasuraUser.this);
                        if (listener != null) {
                            listener.onSuccess(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }


    @Override
    public void changeUsername(final String newUsername, final ChangeUserNameResponseListener listener) {
        userApiService.changeUserName(new ChangeUserNameRequest(newUsername))
                .enqueue(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        setUsername(newUsername);
                        HasuraSessionStore.saveUser(HasuraUser.this);
                        if (listener != null) {
                            listener.onSuccess(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    @Override
    public void logout(final LogoutResponseListener listener) {
        userApiService.logout()
                .enqueue(new Callback<LogoutResponse, HasuraException>() {
                    @Override
                    public void onSuccess(LogoutResponse response) {
                        HasuraSessionStore.deleteSavedUser();
                        setAuthToken(null);
                        if (listener != null) {
                            listener.onSuccess(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    public void forceLogout() {
        clearAllData();
        HasuraSessionStore.deleteSavedUser();
    }

    private void clearAllData() {
        setId(-1);
        setEmail(null);
        setUsername(null);
        setMobile(null);
        setRoles(null);
        setAuthToken(null);
        setPassword(null);
    }

    @Override
    public void deleteAccount(final DeleteAccountResponseListener listener) {
        userApiService.deleteAccount(new DeleteAccountRequest(password))
                .enqueue(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        HasuraSessionStore.deleteSavedUser();
                        setAuthToken(null);
                        if (listener != null) {
                            listener.onSuccess(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }


    @Override
    public String toString() {
        return "HasuraUser {" +
                "  id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roles=" + roles +
                ", authToken='" + authToken + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
