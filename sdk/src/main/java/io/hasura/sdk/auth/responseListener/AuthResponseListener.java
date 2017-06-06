package io.hasura.sdk.auth.responseListener;

import io.hasura.sdk.core.HasuraException;
import io.hasura.sdk.auth.HasuraUser;

/**
 * Created by jaison on 31/05/17.
 */

public interface AuthResponseListener {
    void onSuccess(HasuraUser user);
    void onFailure(HasuraException e);
}
