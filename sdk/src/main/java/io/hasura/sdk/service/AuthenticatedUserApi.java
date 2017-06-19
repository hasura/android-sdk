package io.hasura.sdk.service;

import io.hasura.sdk.responseListener.ChangeEmailResponseListener;
import io.hasura.sdk.responseListener.ChangeMobileResponseListener;
import io.hasura.sdk.responseListener.ChangePasswordResponseListener;
import io.hasura.sdk.responseListener.ChangeUserNameResponseListener;
import io.hasura.sdk.responseListener.DeleteAccountResponseListener;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.sdk.responseListener.SyncStatusListener;

/**
 * Created by jaison on 19/06/17.
 */

public interface AuthenticatedUserApi {

    void sync(SyncStatusListener listener);

    void changePassword(String newPassword, ChangePasswordResponseListener listener);

    void changeEmail(String newEmail, ChangeEmailResponseListener listener);

    void changeMobile(String newMobileNumber, ChangeMobileResponseListener listener);

    void deleteAccount(DeleteAccountResponseListener listener);

    void changeUsername(String newUsername, ChangeUserNameResponseListener listener);

    void logout(LogoutResponseListener listener);

}
