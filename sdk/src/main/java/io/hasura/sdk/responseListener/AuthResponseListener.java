package io.hasura.sdk.responseListener;

import io.hasura.sdk.HasuraException;
import io.hasura.sdk.HasuraUser;

/**
 * Created by jaison on 31/05/17.
 */

public interface AuthResponseListener {
    void onSuccess(HasuraUser user);
    void onFailure(HasuraException e);
}
