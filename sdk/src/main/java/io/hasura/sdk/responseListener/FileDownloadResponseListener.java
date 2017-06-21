package io.hasura.sdk.responseListener;

import io.hasura.sdk.exception.HasuraException;

public interface FileDownloadResponseListener {
    void onDownloadComplete(byte[] data);
    void onDownloadFailed(HasuraException e);
    void onDownloading(float completedPercentage);
}