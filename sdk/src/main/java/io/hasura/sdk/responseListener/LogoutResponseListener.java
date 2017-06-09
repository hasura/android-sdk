package io.hasura.sdk.responseListener;

import io.hasura.sdk.HasuraException;

/**
 * Created by jaison on 31/05/17.
 */

public interface LogoutResponseListener {
    void onSuccess();
    void onFailure(HasuraException e);
}
