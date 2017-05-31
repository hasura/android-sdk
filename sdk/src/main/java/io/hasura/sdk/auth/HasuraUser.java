package io.hasura.sdk.auth;

import io.hasura.sdk.auth.request.AuthRequest;
import io.hasura.sdk.auth.response.AuthResponse;
import io.hasura.sdk.auth.response.LogoutResponse;
import io.hasura.sdk.auth.response.MessageResponse;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.auth.responseListener.LogoutResponseListener;
import io.hasura.sdk.auth.responseListener.OtpStatusListener;
import io.hasura.sdk.auth.service.AnonymousUserService;
import io.hasura.sdk.auth.service.HasuraUserService;
import io.hasura.sdk.core.Callback;

/**
 * Created by jaison on 30/05/17.
 */

public class HasuraUser {

    private Integer id;
    private String email;
    private String username;
    private String mobile;
    private String[] roles;
    private String authToken;
    private String password;
    private String accessToken;

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

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isMobileOtpLoginEnabled() {
        return isMobileOtpLoginEnabled;
    }

    private boolean isMobileOtpLoginEnabled = false;

    private AnonymousUserService anonApiService = AnonymousUserService.getInstance();
    private HasuraUserService userApiService = HasuraUserService.getInstance();

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public void enableMobileOtpLogin() {
        this.isMobileOtpLoginEnabled = true;
    }

    public void signUp(final AuthResponseListener listener) {
        if (isMobileOtpLoginEnabled) {
            anonApiService.signUpForMobileOtp(getAuthRequest())
                    .enqueue(new AuthResponseCallbackHandler(listener));
        } else {
            anonApiService.signUp(getAuthRequest())
                    .enqueue(new AuthResponseCallbackHandler(listener));
        }
    }

    private AuthRequest getAuthRequest() {
        return new AuthRequest(this.username, this.email, this.mobile, this.password);
    }

    public void otpLogin(final AuthResponseListener listener) {
        anonApiService.login(getAuthRequest())
                .enqueue(new AuthResponseCallbackHandler(listener));
    }

    public void sendOtpToMobile(final OtpStatusListener listener) {
        anonApiService.sendOtpToMobile(getAuthRequest())
                .enqueue(new Callback<MessageResponse, AuthException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }

                    @Override
                    public void onFailure(AuthException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    public void otpLogin(String otp, final AuthResponseListener listener) {
        AuthRequest request = new AuthRequest(mobile, otp);
        anonApiService.otpLogin(request)
                .enqueue(new AuthResponseCallbackHandler(listener));
    }

    class AuthResponseCallbackHandler implements Callback<AuthResponse, AuthException> {

        AuthResponseListener listener;

        AuthResponseCallbackHandler(AuthResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(AuthResponse response) {
            id = response.getId();
            roles = response.getRoles();
            authToken = response.getAuthToken();
            accessToken = response.getAccess_token();

            HasuraSessionStore.saveUser(HasuraUser.this);

            if (listener != null) {
                listener.onSuccess(HasuraUser.this);
            }
        }

        @Override
        public void onFailure(AuthException e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    public void logout(final LogoutResponseListener listener) {
        userApiService.logout()
                .enqueue(new Callback<LogoutResponse, AuthException>() {
                    @Override
                    public void onSuccess(LogoutResponse response) {
                        HasuraSessionStore.deleteSavedUser();
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }

                    @Override
                    public void onFailure(AuthException e) {
                        if (listener != null) {
                            listener.onFailure(e);
                        }
                    }
                });
    }

    @Override
    public String toString() {
        return "HasuraUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roles=" + roles +
                ", authToken='" + authToken + '\'' +
                ", password='" + password + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", isMobileOtpLoginEnabled=" + isMobileOtpLoginEnabled +
                '}';
    }
}
