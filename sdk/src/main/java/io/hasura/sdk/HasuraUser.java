package io.hasura.sdk;

import java.util.HashMap;
import java.util.Map;

import io.hasura.sdk.request.AuthRequest;
import io.hasura.sdk.request.SocialLoginRequest;
import io.hasura.sdk.response.AuthResponse;
import io.hasura.sdk.response.LogoutResponse;
import io.hasura.sdk.response.MessageResponse;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.sdk.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.sdk.service.AnonymousUserService;
import io.hasura.sdk.service.HasuraUserService;

/**
 * Created by jaison on 30/05/17.
 */

public class HasuraUser {

    private AnonymousUserService anonApiService = AnonymousUserService.getInstance();

    public void signUp(final AuthResponseListener listener) {
        if (isMobileOtpLoginEnabled) {
            anonApiService.signUpForMobileOtp(getAuthRequest())
                    .executeAsync(new AuthResponseCallbackHandler(listener));
        } else {
            anonApiService.signUp(getAuthRequest())
                    .executeAsync(new AuthResponseCallbackHandler(listener));
        }
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
            setSelectedRole(HasuraConfig.USER.DEFAULT_ROLE);

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

    private HasuraTokenInterceptor hasuraTokenInterceptor = new HasuraTokenInterceptor();
    private HasuraUserService userApiService;

    private HasuraUserService auth() {
        if (userApiService == null) {
            userApiService = new HasuraUserService(hasuraTokenInterceptor);
        }
        return userApiService;
    }

    public <K> K getCustomService(String serviceName, Class<K> clzz) {
        return clzz.cast(Hasura.getInstance().getService(serviceName).getInterface(hasuraTokenInterceptor));
    }

    public HasuraQuery.Builder getQueryBuilder() {
        return new HasuraQuery.Builder(hasuraTokenInterceptor);
    }

    private AuthRequest getAuthRequest() {
        return new AuthRequest(this.username, this.email, this.mobile, this.password);
    }

    public void logout(final LogoutResponseListener listener) {
        auth().logout()
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


    private Integer id;
    private String email;
    private String username;
    private String mobile;
    private String[] roles;
    private String selectedRole;
    private String authToken;
    private String password;
    private String accessToken;
    private boolean isMobileOtpLoginEnabled = false;

    private HashMap<String, HasuraUser> otherRoleUsers = new HashMap<>();

    public HasuraUser() {

    }

    public HasuraUser(HasuraUser user) {
        this.setId(user.getId());
        setEmail(user.getEmail());
        setUsername(user.getUsername());
        setMobile(user.getMobile());
        setRoles(user.getRoles());
        setAuthToken(user.getAuthToken());
        setPassword(user.getPassword());
        setAccessToken(user.getAccessToken());
        if (user.isMobileOtpLoginEnabled())
            user.enableMobileOtpLogin();
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

    public String getAccessToken() {
        return accessToken;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public boolean isMobileOtpLoginEnabled() {
        return isMobileOtpLoginEnabled;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public void setSelectedRole(String selectedRole) {
        this.hasuraTokenInterceptor.setRole(selectedRole);
        this.selectedRole = selectedRole;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        this.hasuraTokenInterceptor.setAuthToken(authToken);

        //Changing the authtoken for all the other role users
        for (Map.Entry<String, HasuraUser> entry : otherRoleUsers.entrySet()) {
            HasuraUser user = entry.getValue();
            user.setAuthToken(authToken);
        }
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

    public HasuraUser asRole(String role) {
        if (otherRoleUsers.containsKey(role)) {
            return otherRoleUsers.get(role);
        }
        HasuraUser newUser = new HasuraUser(this);
        newUser.setSelectedRole(role);
        otherRoleUsers.put(role, newUser);
        return newUser;
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
                ", accessToken='" + accessToken + '\'' +
                ", isMobileOtpLoginEnabled=" + isMobileOtpLoginEnabled +
                '}';
    }

}
