package io.hasura.sdk.service;

import io.hasura.sdk.responseListener.EmailVerificationSenderListener;
import io.hasura.sdk.HasuraSocialLoginType;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.responseListener.MobileConfirmationResponseListener;
import io.hasura.sdk.responseListener.OtpStatusListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;

/**
 * Created by jaison on 19/06/17.
 */

public interface AnonymousUserApi {

    void signUp(SignUpResponseListener listener);

    void otpSignUp(SignUpResponseListener listener);

    void login(AuthResponseListener listener);

    void sendOtpToMobile(OtpStatusListener listener);

    void otpLogin(String otp, AuthResponseListener listener);

    void socialLogin(HasuraSocialLoginType type, String token, final AuthResponseListener listener);

    void confirmMobile(String otp, MobileConfirmationResponseListener listener);

    void confirmMobileAndLogin(String otp, final AuthResponseListener listener);

    void resendOTP(OtpStatusListener listener);

    void resendVerificationEmail(EmailVerificationSenderListener listener);

    void resendOtpForMobileConfirmation(OtpStatusListener listener);
}