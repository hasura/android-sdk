package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

public class ConfirmEmailResponse {
    @SerializedName("hasura_id")
    int hasuraId;

    @SerializedName("user_email")
    String userEmail;

    @SerializedName("message")
    String message;

    public int getHasuraId() {
      return hasuraId;
    }

    public String getUserEmail() {
      return userEmail;
    }

    public String getMessage() {
      return message;
    }
}
