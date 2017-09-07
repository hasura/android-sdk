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

    void logout(LogoutResponseListener listener);

}
