package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 25/05/17.
 */

public class AuthResponse {
    @SerializedName("auth_token")
    private String authToken;

    @SerializedName("hasura_id")
    private Integer id;

    @SerializedName("hasura_roles")
    private String[] roles;

    @SerializedName("new_user")
    private boolean newUser;

    @SerializedName("access_token")
    private String access_token;

    public boolean isNewUser() {
        return newUser;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getId() {
        return id;
    }

    public String[] getRoles() {
        return roles;
    }




    @Override
    public String toString() {
        return "AuthResponse{" +
                "authToken='" + authToken + '\'' +
                ", id=" + id +
                ", roles=" + roles +
                ", newUser=" + newUser +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
