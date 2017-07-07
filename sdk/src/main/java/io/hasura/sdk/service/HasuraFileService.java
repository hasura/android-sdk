package io.hasura.sdk.service;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import io.hasura.sdk.Call;
import io.hasura.sdk.Callback;
import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.responseConverter.HasuraFileResponseConverter;
import io.hasura.sdk.responseConverter.HasuraResponseConverter;
import io.hasura.sdk.HttpClientProvider;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.FileUploadResponse;
import io.hasura.sdk.query.HasuraQuery;
import io.hasura.sdk.responseListener.FileUploadResponseListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jaison on 21/06/17.
 */

public class HasuraFileService {

    String role;
    HttpClientProvider httpClientProvider;
    ProjectConfig projectConfig;

    private HasuraFileService(Builder builder) {
        this.httpClientProvider = builder.httpClientProvider;
        this.role = builder.role;
        this.projectConfig = builder.projectConfig;
    }

    public static final class Builder {
        String role;
        HttpClientProvider httpClientProvider;
        ProjectConfig projectConfig;

        public Builder() {
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setHttpClientProvider(HttpClientProvider httpClientProvider) {
            this.httpClientProvider = httpClientProvider;
            return this;
        }

        public Builder setProjectConfi(ProjectConfig config) {
            this.projectConfig = config;
            return this;
        }

        public HasuraFileService build() {
            return new HasuraFileService(this);
        }

    }

    private Request getUploadRequest(String fileName, File file, String contentType) throws IllegalArgumentException {
        MediaType mediaType = MediaType.parse(contentType);
        if (mediaType == null) {
            throw new IllegalArgumentException("Malformed content type : " + contentType);
        }
        RequestBody body = RequestBody.create(mediaType, file);
        return new Request.Builder()
                .url(projectConfig.getUploadFileUrl(fileName))
                .post(body)
                .build();
    }

    private Request getUploadRequest(String fileName, byte[] file, String contentType) throws IllegalArgumentException {
        MediaType mediaType = MediaType.parse(contentType);
        if (mediaType == null) {
            throw new IllegalArgumentException("Malformed content type : " + contentType);
        }
        RequestBody body = RequestBody.create(mediaType, file);
        return new Request.Builder()
                .url(projectConfig.getUploadFileUrl(fileName))
                .post(body)
                .build();
    }

    public void uploadFile(byte[] file, String contentType, FileUploadResponseListener listener) throws IllegalArgumentException {
        uploadFile(UUID.randomUUID().toString(), file, contentType, listener);
    }

    public void uploadFile(File file, String contentType, final FileUploadResponseListener listener) throws IllegalArgumentException {
        uploadFile(UUID.randomUUID().toString(), file, contentType, listener);
    }

    public void uploadFile(String fileId, byte[] file, String contentType, FileUploadResponseListener listener) throws IllegalArgumentException {
        Request request = getUploadRequest(fileId, file, contentType);
        uploadFile(request, listener);
    }

    public void uploadFile(String fileId, File file, String contentType, final FileUploadResponseListener listener) throws IllegalArgumentException {
        Request request = getUploadRequest(fileId, file, contentType);
        uploadFile(request, listener);
    }

    private void uploadFile(Request request, final FileUploadResponseListener listener) {
        OkHttpClient client = httpClientProvider.getClientForRole(role);
        Call<FileUploadResponse, HasuraException> call = new Call<>(client.newCall(request), new HasuraResponseConverter<>(FileUploadResponse.class));
        call.enqueue(new Callback<FileUploadResponse, HasuraException>() {
            @Override
            public void onSuccess(FileUploadResponse response) {
                if (listener != null) {
                    listener.onUploadComplete(response);
                }
            }

            @Override
            public void onFailure(HasuraException e) {
                if (listener != null) {
                    listener.onUploadFailed(e);
                }
            }
        });
    }

    public void downloadFile(String fileId, final FileDownloadResponseListener listener) {
        downloadFileWithUrl(projectConfig.getDownloadFileUrl(fileId), listener);
    }

    public void downloadFileWithUrl(String url, final FileDownloadResponseListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = httpClientProvider.getClientForRole(role, listener);
        new Call<>(client.newCall(request), new HasuraFileResponseConverter())
                .enqueue(new Callback<byte[], HasuraException>() {
                    @Override
                    public void onSuccess(byte[] response) {
                        if (listener != null)
                            listener.onDownloadComplete(response);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        if (listener != null)
                            listener.onDownloadFailed(e);
                    }
                });
    }

}
