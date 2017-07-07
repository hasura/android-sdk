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
    public Call<GetCredentialsResponse, HasuraException> getCredentials() {
        Type respType = new TypeToken<GetCredentialsResponse>() {
        }.getType();
        return makeGetCall("user/account/info", respType);
    }

    /**
     * Change user's password
     * <p>
     *     This method takes a {@link ChangePasswordRequest} object, which should contain the
     *     current password and the new password.
     * </p>
     *
     * @param r {@link ChangePasswordRequest}
     * @return  {@link ChangePasswordResponse}
     * @throws HasuraException
     */
    public Call<ChangePasswordResponse, HasuraException> changePassword(ChangePasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangePasswordResponse>() {
        }.getType();
        return makePostCall("user/password/change", jsonBody, respType);
    }

    /**
     * Change user's email address.
     * <p>
     *     Initialize {@link ChangeEmailRequest} with the new email address of the user. This method
     *     will send a verification email to the new email address of the user.
     * </p>
     *
     * @param r {@link ChangeEmailRequest}
     * @return  {@link ChangeEmailResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> changeEmail(ChangeEmailRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangeEmailResponse>() {
        }.getType();
        return makePostCall("user/email/change", jsonBody, respType);
    }

    /**
     * Change user's mobile number. This method will send an OTP to the new number of the user.
     *
     * @param r {@link ChangeMobileRequest}
     * @return  {@link ChangeMobileResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> changeMobile(ChangeMobileRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangeMobileResponse>() {
        }.getType();
        return makePostCall("user/mobile/change", jsonBody, respType);
    }


    /**
     *
     * @param r
     * @return
     */
    public Call<MessageResponse, HasuraException> verifyPassword(CheckPasswordRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<CheckPasswordResponse>() {
        }.getType();
        return makePostCall("user/password/verify", jsonBody, respType);
    }


    /**
     * Delete account of the current user
     *
     * @param r {@link DeleteAccountRequest}
     * @return  {@link DeleteAccountResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> deleteAccount(DeleteAccountRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<DeleteAccountResponse>() {
        }.getType();
        return makePostCall("user/account/delete", jsonBody, respType);
    }

    /**
     * Change user's username.
     *
     * @param r {@link ChangeUserNameRequest}
     * @return  {@link ChangeUserNameResponse}
     * @throws HasuraException
     */
    public Call<MessageResponse, HasuraException> changeUserName(ChangeUserNameRequest r) {
        String jsonBody = gson.toJson(r);
        Type respType = new TypeToken<ChangeUserNameResponse>() {
        }.getType();
        return makePostCall("user/account/change-username", jsonBody, respType);
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
