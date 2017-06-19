package io.hasura.sdk.responseListener;

import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.HasuraUser;

/**
 * Created by jaison on 16/06/17.
 */

public interface SignUpResponseListener {
    void onSuccessAwaitingVerification(HasuraUser user);
    void onSuccess(HasuraUser user);
    void onFailure(HasuraException e);
}
