package io.hasura.android_sdk.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import io.hasura.sdk.HasuraErrorCode;
import io.hasura.sdk.exception.HasuraException;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
    }

    protected void showErrorAlert(String message, DialogInterface.OnClickListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        if (listener != null) {
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Dismiss", listener);
        } else {
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
        }
        alertDialog.show();
    }

    protected void showProgressIndicator() {
        pd.setMessage("Please wait");
        pd.show();
    }

    protected void hideProgressIndicator() {
        pd.dismiss();
    }

    public void handleError(HasuraException e) {
        if (e.getCode() == HasuraErrorCode.UNAUTHORISED) {
            showErrorAlert("You login session has expired. Please log in again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    completeUserLogout();

                }
            });
        } else
            showErrorAlert(e.getMessage(), null);
    }

    public void completeUserLogout() {
        AuthenticationActivity.startActivity(this);
    }
}
