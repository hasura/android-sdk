package io.hasura.android_sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.hasura.android_sdk.R;
import io.hasura.android_sdk.models.TodoReturningResponse;
import io.hasura.sdk.auth.HasuraUser;
import io.hasura.sdk.auth.response.MessageResponse;
import io.hasura.sdk.auth.responseListener.AuthResponseListener;
import io.hasura.sdk.core.Call;
import io.hasura.sdk.core.Callback;
import io.hasura.sdk.core.Hasura;
import io.hasura.sdk.core.HasuraException;


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

        Hasura.setProjectName("hello70")
                .enableLogs()
                .initialise(this);

        setContentView(R.layout.activity_authentication);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.signInButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        username.setText("jaison");
        password.setText("password");

        user = new HasuraUser();
        user.setMobile("8861503583");

        if (Hasura.currentUser() != null) {
            //Logged in user is present
            Log.i(TAG,"Logged in present: " + Hasura.currentUser().toString());
            ToDoActivity.startActivity(this);
        }

        Call<TodoReturningResponse,HasuraException> call1 = user.customService("serviceName")
                .POST("/something/blah")
                .setParams("key","value")
                .setParams("key","value")
                .build();

        call1.executeAsync(new Callback<TodoReturningResponse, HasuraException>() {
            @Override
            public void onSuccess(TodoReturningResponse response) {

            }

            @Override
            public void onFailure(HasuraException e) {

            }
        });

        Call<MessageResponse, HasuraException> call3 = user.queryTemplateService("qTempName")
                .setParams("key","value")
                .build();
        call3.executeAsync(new Callback<MessageResponse, HasuraException>() {
            @Override
            public void onSuccess(MessageResponse response) {

            }

            @Override
            public void onFailure(HasuraException e) {

            }
        });

    }

    private void signUp() {
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUp(new AuthResponseListener() {
            @Override
            public void onSuccess(HasuraUser user) {
                ToDoActivity.startActivity(AuthenticationActivity.this);
            }

            @Override
            public void onFailure(HasuraException e) {
                handleError(e);
            }
        });
    }

    private void login() {
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.login(new AuthResponseListener() {
            @Override
            public void onSuccess(HasuraUser user) {
                ToDoActivity.startActivity(AuthenticationActivity.this);
            }

            @Override
            public void onFailure(HasuraException e) {
                handleError(e);
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
                signUp();
                break;
        }
    }
}
