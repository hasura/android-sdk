package io.hasura.sdk.authProvider;

import io.hasura.sdk.Callback;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.MessageResponse;
import io.hasura.sdk.service.MobileAuthProviderAPIService;

/**
 * Created by jaison on 06/09/17.
 */

public class MobileAuthProvider implements HasuraAuthProvider<MobileOtpRecord> {

    private String mobileNumber;
    private String otp;

    private static MobileAuthProviderAPIService apiService;

    public static void setApiService(MobileAuthProviderAPIService service) {
        apiService = service;
    }

    public static void sendOtp(String mobileNumber, final OtpStatusListener listener) {
        apiService.sendOtp(mobileNumber).enqueue(new Callback<MessageResponse, HasuraException>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if (listener != null) {
                    listener.onOtpSentSuccessfully();
                }
            }

            @Override
            public void onFailure(HasuraException e) {
                if (listener != null) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public static void resendOtp(String mobileNumber, final OtpStatusListener listener) {
        apiService.resendOtp(mobileNumber).enqueue(new Callback<MessageResponse, HasuraException>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if (listener != null) {
                    listener.onOtpSentSuccessfully();
                }
            }

            @Override
            public void onFailure(HasuraException e) {
                if (listener != null) {
                    listener.onFailure(e);
                }
            }
        });
    }

    public MobileAuthProvider(String mobileNumber, String otp) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
    }

    @Override
    public String getProviderType() {
        return "mobile";
    }

    @Override
    public MobileOtpRecord getDataObject() {
        return new MobileOtpRecord(mobileNumber, otp);
    }

    public interface OtpStatusListener {
        void onOtpSentSuccessfully();
        void onFailure(HasuraException e);
    }
}
