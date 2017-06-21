package io.hasura.sdk;

import io.hasura.sdk.responseListener.FileDownloadResponseListener;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 21/06/17.
 */

public interface HttpClientProvider {
    OkHttpClient getClientForRole(String role);
    OkHttpClient getClientForRole(String role, FileDownloadResponseListener listener);
}
