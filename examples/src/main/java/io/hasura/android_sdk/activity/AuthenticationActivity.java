package io.hasura.android_sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.hasura.android_sdk.R;
import io.hasura.sdk.auth.AuthErrorCode;
import io.hasura.sdk.auth.AuthException;
import io.hasura.sdk.auth.Hasura;
import io.hasura.sdk.auth.HasuraSessionStore;
import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.auth.responseListener.OtpStatusListener;
import io.hasura.sdk.temp.HasuraQuery;


public class AuthenticationActivity extends BaseActivity implements View.OnClickListener {

    EditText username, password;
    Button signInButton, registerButton;

    HasuraUser user;

    private static String TAG = "AuthActivity";

    public static void startActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity, AuthenticationActivity.class));
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Hasura.initialise(this, "hello70");
        setContentView(R.layout.activity_authentication);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.signInButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);



        if (Hasura.currentUser() != null) {
            //Logged in user is present
            Log.i(TAG,"Logged in present: " + Hasura.currentUser().toString());
            user = Hasura.currentUser();
        } else {
            //No logged In user
            Log.i(TAG,"No logged in user");
            user = new HasuraUser();
            user.setUsername("jaison");
            user.setMobile("8861503583");
            user.enableMobileOtpLogin();

            sendOtp();
        }

    }

    private void signUp() {
        user.signUp(new AuthResponseListener() {
            @Override
            public void onSuccess(HasuraUser user) {
            }

            @Override
            public void onFailure(AuthException e) {

            }
        });
    }

    private void login() {
        user.otpLogin(username.getText().toString(), new AuthResponseListener() {
            @Override
            public void onSuccess(HasuraUser user) {
                //Sign in successfully completed.
                //Hasura.currentUser(); will now give you a logged in user.
            }

            @Override
            public void onFailure(AuthException e) {

            }
        });
    }

    private void sendOtp() {
        user.sendOtpToMobile(new OtpStatusListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(AuthException e) {
                if (e.getCode() == AuthErrorCode.INVALID_USER) {
                    //Unregistered Number
                    signUp();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                login();
                break;
            case R.id.registerButton:
                HasuraSessionStore.deleteSavedUser();
//                user.logout(new LogoutResponseListener() {
//                    @Override
//                    public void onSuccess() {
//                        Log.i(TAG, "Logout");
//                    }
//
//                    @Override
//                    public void onFailure(AuthException e) {
//                        Log.i(TAG, "Logout - Failed");
//                    }
//                });
                break;
        }
    }
}
