package io.hasura.sdk.auth.responseListener;

import io.hasura.sdk.auth.AuthException;
import io.hasura.sdk.auth.HasuraUser;

/**
 * Created by jaison on 31/05/17.
 */

public interface AuthResponseListener {
    void onSuccess(HasuraUser user);
    void onFailure(AuthException e);
}
