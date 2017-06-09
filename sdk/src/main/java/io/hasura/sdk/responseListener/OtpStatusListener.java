package io.hasura.sdk.responseListener;

import io.hasura.sdk.HasuraException;

/**
 * Created by jaison on 31/05/17.
 */

public interface OtpStatusListener {
    void onSuccess();
    void onFailure(HasuraException e);
}
