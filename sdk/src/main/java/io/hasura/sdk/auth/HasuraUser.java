package io.hasura.sdk.auth;

import io.hasura.sdk.auth.request.AuthRequest;
import io.hasura.sdk.auth.request.ResendOTPRequest;
import io.hasura.sdk.auth.request.SocialLoginRequest;
import io.hasura.sdk.auth.response.AuthResponse;
import io.hasura.sdk.auth.response.LogoutResponse;
import io.hasura.sdk.auth.response.MessageResponse;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.auth.responseListener.LogoutResponseListener;
import io.hasura.sdk.auth.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.auth.responseListener.OtpStatusListener;
import io.hasura.sdk.auth.service.AnonymousUserService;
import io.hasura.sdk.auth.service.CustomService;
import io.hasura.sdk.auth.service.DBService;
import io.hasura.sdk.auth.service.HasuraUserService;
import io.hasura.sdk.auth.service.QueryTemplateService;
import io.hasura.sdk.core.Callback;
import io.hasura.sdk.core.HasuraException;
import io.hasura.sdk.core.HasuraSessionStore;
import io.hasura.sdk.core.HasuraTokenInterceptor;

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
    private boolean isMobileOtpLoginEnabled = false;

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
        hasuraTokenInterceptor.setAuthToken(authToken);
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

    private HasuraTokenInterceptor hasuraTokenInterceptor = new HasuraTokenInterceptor();
    private AnonymousUserService anonApiService = AnonymousUserService.getInstance();
    private HasuraUserService userApiService;
    private DBService dbService;
    private QueryTemplateService qtService;

    public void enableMobileOtpLogin() {
        this.isMobileOtpLoginEnabled = true;
    }

    public void signUp(final AuthResponseListener listener) {
        if (isMobileOtpLoginEnabled) {
            anonApiService.signUpForMobileOtp(getAuthRequest())
                    .executeAsync(new AuthResponseCallbackHandler(listener));
        } else {
            anonApiService.signUp(getAuthRequest())
                    .executeAsync(new AuthResponseCallbackHandler(listener));
        }
    }

    private AuthRequest getAuthRequest() {
        return new AuthRequest(this.username, this.email, this.mobile, this.password);
    }

    public void sendOtpToMobile(final OtpStatusListener listener) {
        anonApiService.sendOtpToMobile(getAuthRequest())
                .executeAsync(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        if (listener != null) {
                            listener.onSuccess();
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

    public void otpLogin(String otp, final AuthResponseListener listener) {
        AuthRequest request = new AuthRequest(mobile, otp);
        anonApiService.otpLogin(request)
                .executeAsync(new AuthResponseCallbackHandler(listener));
    }

    public void login(final AuthResponseListener listener) {
        anonApiService.login(getAuthRequest())
                .executeAsync(new AuthResponseCallbackHandler(listener));
    }

    public void socialLogin(HasuraSocialLoginType type, String token, final AuthResponseListener listener) {
        anonApiService.socialAuth(new SocialLoginRequest(type.getCode(), token))
                .executeAsync(new AuthResponseCallbackHandler(listener));
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
            setAccessToken(response.getAccess_token());

            HasuraSessionStore.saveUser(HasuraUser.this);

            if (listener != null) {
                listener.onSuccess(HasuraUser.this);
            }
        }

        @Override
        public void onFailure(HasuraException e) {
            if (listener != null) {
                listener.onFailure(e);
            }
        }
    }

    public void confirmMobile(String otp, final MobileConfirmationResponseListener listener) {
        anonApiService.confirmMobile(mobile, otp)
                .executeAsync(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        if (listener != null) {
                            listener.onSuccess();
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

    public void resendOtpForMobileConfirmation(final OtpStatusListener listener) {
        anonApiService.resendOTP(mobile)
                .executeAsync(new Callback<MessageResponse, HasuraException>() {
                    @Override
                    public void onSuccess(MessageResponse response) {
                        if (listener != null) {
                            listener.onSuccess();
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

    public void logout(final LogoutResponseListener listener) {
        getUserService().logout()
                .executeAsync(new Callback<LogoutResponse, HasuraException>() {
                    @Override
                    public void onSuccess(LogoutResponse response) {
                        HasuraSessionStore.deleteSavedUser();
                        setAuthToken(null);
                        if (listener != null) {
                            listener.onSuccess();
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

    private HasuraUserService getUserService() {
        if (userApiService == null) {
            userApiService = new HasuraUserService(hasuraTokenInterceptor);
        }
        return userApiService;
    }

    public DBService dataService() {
        if (dbService == null) {
            dbService = new DBService(hasuraTokenInterceptor);
        }
        return dbService;
    }

    public CustomService customService(String serviceName) {
        return new CustomService(serviceName, hasuraTokenInterceptor);
    }

    public QueryTemplateService.Builder queryTemplateService(String templateName) {
        if (qtService == null) {
            qtService = new QueryTemplateService(hasuraTokenInterceptor);
        }
        return qtService.getBuilder(templateName);
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
