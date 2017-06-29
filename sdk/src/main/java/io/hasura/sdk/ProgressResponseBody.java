package io.hasura.sdk;

import android.util.Log;

import java.io.IOException;

import io.hasura.sdk.responseListener.FileDownloadResponseListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final FileDownloadResponseListener fileDownloadResponseListener;
    private BufferedSource bufferedSource;

    private String TAG = "ProgressResponseBody";

    ProgressResponseBody(ResponseBody responseBody, FileDownloadResponseListener fileDownloadResponseListener) {
        this.responseBody = responseBody;
        this.fileDownloadResponseListener = fileDownloadResponseListener;
        Log.d(TAG, "Constructor");

    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.d(TAG, "TotalBytesRead : " + totalBytesRead);
                if (fileDownloadResponseListener != null) {
                    fileDownloadResponseListener.onDownloading((100 * totalBytesRead) / responseBody.contentLength());
                    Log.d(TAG, "Download Progress : " + (100 * totalBytesRead) / responseBody.contentLength());
                }
                return bytesRead;
            }
        };
    }
}