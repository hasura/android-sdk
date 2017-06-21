package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

public class FileUploadResponse {

    @SerializedName("file_id")
    private String file_id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("created_at")
    private String created_at;

    public String getFile_id() {
        return file_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }
}