package io.hasura.sdk.auth.responseListener;

import io.hasura.sdk.core.HasuraException;

/**
 * Created by jaison on 06/06/17.
 */

public interface MobileConfirmationResponseListener {
    void onSuccess();
    void onFailure(HasuraException e);
}
