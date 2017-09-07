package io.hasura.sdk.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by jaison on 25/05/17.
 */

public class AuthResponse {

    @SerializedName("hasura_id")
    private Integer id;

    @SerializedName("hasura_roles")
    private String[] roles;

    @SerializedName("auth_token")
    private String authToken;

    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("username")
    private String username;

    @SerializedName("new_user")
    boolean newUser;

    @SerializedName("access_token")
    String access_token;

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

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "id=" + id +
                ", roles=" + Arrays.toString(roles) +
                ", authToken='" + authToken + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", username='" + username + '\'' +
                ", newUser=" + newUser +
                ", access_token='" + access_token + '\'' +
                '}';
    }

}
