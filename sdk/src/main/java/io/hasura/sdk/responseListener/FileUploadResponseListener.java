package io.hasura.sdk.responseListener;

import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.FileUploadResponse;

/**
 * Created by jaison on 21/06/17.
 */

public interface FileUploadResponseListener {
    void onUploadComplete(FileUploadResponse response);
    void onUploadFailed(HasuraException e);
}
