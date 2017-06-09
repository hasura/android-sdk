package io.hasura.sdk.responseListener;

import io.hasura.sdk.HasuraException;

/**
 * Created by jaison on 06/06/17.
 */

public interface MobileConfirmationResponseListener {
    void onSuccess();
    void onFailure(HasuraException e);
}
