package io.hasura.sdk.auth.responseListener;

import io.hasura.sdk.auth.AuthException;

/**
 * Created by jaison on 31/05/17.
 */

public interface OtpStatusListener {
    void onSuccess();
    void onFailure(AuthException e);
}
