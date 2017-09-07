package io.hasura.sdk.authProvider;

import io.hasura.sdk.Callback;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.MessageResponse;
import io.hasura.sdk.service.EmailAuthProviderAPIService;

/**
 * Created by jaison on 06/09/17.
 */

public class EmailAuthProvider implements HasuraAuthProvider<EmailPasswordRecord> {

    private String email;
    private String password;

    public EmailAuthProvider(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getProviderType() {
        return "email";
    }

    @Override
    public EmailPasswordRecord getDataObject() {
        return new EmailPasswordRecord(email, password);
    }


    private static EmailAuthProviderAPIService apiService;
    public static void setApiService(EmailAuthProviderAPIService api) {
        apiService = api;
    }

    public static void resendEmailVerification(String email, final VerificationEmailStatusListener listener) {
        apiService.resendEmailVerification(email).enqueue(new Callback<MessageResponse, HasuraException>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if (listener != null)
                    listener.onVerificationEmailSentSuccessfully(response.getMessage());
            }

            @Override
            public void onFailure(HasuraException e) {
                if (listener != null)
                    listener.onFailure(e);
            }
        });
    }

    interface VerificationEmailStatusListener {
        void onVerificationEmailSentSuccessfully(String message);
        void onFailure(HasuraException e);
    }

    public static void forgotPassword(String email, final ResetPasswordEmailStatusListener listener) {
        apiService.forgotPassword(email).enqueue(new Callback<MessageResponse, HasuraException>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if (listener != null)
                    listener.onResetPasswordEmailSentSuccessfully(response.getMessage());
            }

            @Override
            public void onFailure(HasuraException e) {
                if (listener != null)
                    listener.onFailure(e);
            }
        });
    }

    interface ResetPasswordEmailStatusListener {
        void onResetPasswordEmailSentSuccessfully(String message);
        void onFailure(HasuraException e);
    }
}
