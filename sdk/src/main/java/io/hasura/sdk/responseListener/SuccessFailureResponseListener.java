package io.hasura.sdk.responseListener;

import io.hasura.sdk.exception.HasuraException;

/**
 * Created by jaison on 19/06/17.
 */

public interface SuccessFailureResponseListener {
    void onSuccess(String message);
    void onFailure(HasuraException e);
}
