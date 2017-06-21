package io.hasura.sdk;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by jaison on 21/06/17.
 */

public class ProgressRequestBody extends RequestBody {

    RequestBody requestBody;

    public ProgressRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

    }
}
