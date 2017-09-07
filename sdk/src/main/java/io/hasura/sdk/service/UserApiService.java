package io.hasura.sdk.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.hasura.sdk.Call;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.request.ChangeEmailRequest;
import io.hasura.sdk.model.request.ChangeMobileRequest;
import io.hasura.sdk.model.request.ChangePasswordRequest;
import io.hasura.sdk.model.request.ChangeUserNameRequest;
import io.hasura.sdk.model.request.CheckPasswordRequest;
import io.hasura.sdk.model.request.DeleteAccountRequest;
import io.hasura.sdk.model.response.AuthResponse;
import io.hasura.sdk.model.response.ChangeEmailResponse;
import io.hasura.sdk.model.response.ChangeMobileResponse;
import io.hasura.sdk.model.response.ChangePasswordResponse;
import io.hasura.sdk.model.response.ChangeUserNameResponse;
import io.hasura.sdk.model.response.CheckPasswordResponse;
import io.hasura.sdk.model.response.DeleteAccountResponse;
import io.hasura.sdk.model.response.GetCredentialsResponse;
import io.hasura.sdk.model.response.LogoutResponse;
import io.hasura.sdk.model.response.MessageResponse;
import okhttp3.OkHttpClient;

/**
 * Created by jaison on 31/05/17.
 */

public class UserApiService extends HasuraHttpService {

    public UserApiService(String baseUrl, OkHttpClient httpClient) {
        super(baseUrl, httpClient);
    }

    /**
     * Returns credentials of the logged in user
     * <p>
     *     This method can be used to retrieve HasuraClient credentials for the current logged in user.
     *     HasuraClient credentials include "HasuraClient Id", "Hasura Role" and "Session Id". This method can
     *     also be used to check if the user has an existing session (or logged in basically). If
     *     not logged in, it will throw an {@link HasuraException}.
     * </p>
     *
     * @return {@link GetCredentialsResponse}
     * @throws HasuraException
     */
    public Call<AuthResponse, HasuraException> getCredentials() {
        Type respType = new TypeToken<GetCredentialsResponse>() {}.getType();
        return makeGetCall("user/info", respType);
    }

    /**
     * Logout a logged-in user.
     *
     * @return a {@link LogoutResponse} type
     * @throws HasuraException
     */
    public Call<LogoutResponse, HasuraException> logout() {
        Type respType = new TypeToken<LogoutResponse>() {
        }.getType();
        return makePostCall("user/logout","{}", respType);
    }


}
