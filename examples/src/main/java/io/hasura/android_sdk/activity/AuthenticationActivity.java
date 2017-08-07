package io.hasura.android_sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import io.hasura.android_sdk.ApiInterface;
import io.hasura.android_sdk.R;
import io.hasura.android_sdk.models.SelectTodoRequest;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.custom_service_retrofit.RetrofitCustomService;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraInitException;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.responseListener.AuthResponseListener;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.sdk.responseListener.SignUpResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        //init
        RetrofitCustomService<ApiInterface> cs = new RetrofitCustomService.Builder()
                .serviceName("api")
                .build(ApiInterface.class);

        //init
        try {
            Hasura.setProjectConfig(new ProjectConfig.Builder()
                    .setProjectName("hello70")
                    .build())
                    .addCustomService(cs)
                    .enableLogs()
                    .initialise(this);
        } catch (HasuraInitException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_authentication);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.signInButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        username.setText("jaison");
        password.setText("password");

        user = Hasura.getClient().getUser();

        if (user.isLoggedIn()) {
            //Logged in user is present
            Log.i(TAG, "Logged in present: " + user.toString());
            ToDoActivity.startActivity(this);
        }
        user.setMobile("8861503583");
    }

    private void signUp() {
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUp(new SignUpResponseListener() {
            @Override
            public void onSuccessAwaitingVerification(HasuraUser user) {

            }

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
            public void onSuccess(String message) {
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
