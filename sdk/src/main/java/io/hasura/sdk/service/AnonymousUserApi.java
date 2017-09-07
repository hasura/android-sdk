package io.hasura.sdk.service;

import io.hasura.sdk.authProvider.HasuraAuthProvider;
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

    void signUp(HasuraAuthProvider provider, SignUpResponseListener listener);

    void login(HasuraAuthProvider provider, AuthResponseListener listener);
}