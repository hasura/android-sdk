package io.hasura.android_sdk.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.hasura.android_sdk.ApiInterface;
import io.hasura.android_sdk.R;
import io.hasura.android_sdk.models.DeleteTodoRequest;
import io.hasura.android_sdk.models.InsertTodoRequest;
import io.hasura.android_sdk.models.SelectTodoRequest;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.android_sdk.models.TodoReturningResponse;
import io.hasura.android_sdk.models.UpdateTodoRequest;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.model.response.FileUploadResponse;
import io.hasura.sdk.responseListener.FileDownloadResponseListener;
import io.hasura.sdk.query.HasuraQuery;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.responseListener.FileUploadResponseListener;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.sdk.Callback;
import io.hasura.sdk.exception.HasuraException;
import retrofit2.Call;
import retrofit2.Response;

public class ToDoActivity extends BaseActivity {

    private static String TAG = "ToDoActivity";

    ToDoRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    HasuraClient client = Hasura.getClient();
    HasuraUser user = Hasura.getClient().getUser();

    public static void startActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity, ToDoActivity.class));
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.Interactor() {
            @Override
            public void onTodoClicked(int position, TodoRecord record) {
                toggleTodo(position, record);
            }

            @Override
            public void onTodoLongClicked(int position, TodoRecord record) {
                deleteTodo(position, record);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        fetchTodosFromDB();
    }


    static int PERM_CODE = 123213;

    public boolean shouldAskForPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
    }

    @TargetApi(23)
    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_CODE);
        }
    }


    public void uploadFile() {
        File directory = new File("/storage/3332-6230/DCIM/DCIM/Screenshots");

        if (directory.exists()) {
            Log.i("Files", "Dir exists");
        } else {
            Log.i("Files", "Dir does not exist");
        }

        File[] files = directory.listFiles();

        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
        }


        File file = new File(directory, "photo.png");

        if (file.exists()) {
            Log.i(TAG, "File exists");
        } else {
            Log.i(TAG, "File does not exist");
        }


        client.useFileStoreService()
                .uploadFile("asdsadasdadasdads", file, "image/png", new FileUploadResponseListener() {
                    @Override
                    public void onUploadComplete(FileUploadResponse response) {
                        Log.i(TAG, "Uploaded");
                    }

                    @Override
                    public void onUploadFailed(HasuraException e) {
                        e.printStackTrace();
                        Log.i(TAG, "Upload Failed");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERM_CODE) {

            for (String perm : permissions) {
                Log.i(TAG, perm);
            }

            for (int result : grantResults) {
                Log.i(TAG, result + "");
            }
        }

    }

    private void fetchTodosFromDB() {
        showProgressIndicator();

        //Custom service usage example
//        client.useCustomService(ApiInterface.class)
//                .getTodos(new SelectTodoRequest(user.getId()))
//                .enqueue(new retrofit2.Callback<List<TodoRecord>>() {
//                    @Override
//                    public void onResponse(Call<List<TodoRecord>> call, Response<List<TodoRecord>> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<TodoRecord>> call, Throwable t) {
//
//                    }
//                });

        client.useDataService()
                .setRequestBody(new SelectTodoRequest(user.getId()))
                .expectResponseTypeArrayOf(TodoRecord.class)
                .enqueue(new Callback<List<TodoRecord>, HasuraException>() {
                    @Override
                    public void onSuccess(List<TodoRecord> response) {

                        for (TodoRecord record : response) {
                            Log.i("ResponseRecord", record.toString());
                        }
                        hideProgressIndicator();
                        adapter.setData(response);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressIndicator();
                        handleError(e);
                    }
                });
    }

    private void toggleTodo(final int recyclerViewPostion, final TodoRecord record) {
        showProgressIndicator();
        record.setCompleted(!record.getCompleted());
        UpdateTodoRequest request = new UpdateTodoRequest(record.getId(), user.getId(), record.getTitle(), record.getCompleted());

        HasuraQuery<TodoReturningResponse> updateTodoQuery = client
                .useDataService()
                .setRequestBody(request)
                .expectResponseType(TodoReturningResponse.class);

        updateTodoQuery.enqueue(new Callback<TodoReturningResponse, HasuraException>() {
            @Override
            public void onSuccess(TodoReturningResponse response) {
                hideProgressIndicator();
                adapter.updateData(recyclerViewPostion, record);
            }

            @Override
            public void onFailure(HasuraException e) {
                hideProgressIndicator();
                handleError(e);
            }
        });
    }

    private void deleteTodo(final int recyclerViewPosition, final TodoRecord record) {
        showProgressIndicator();

        client.useDataService()
                .setRequestBody(new DeleteTodoRequest(record.getId(), user.getId()))
                .expectResponseType(TodoReturningResponse.class)
                .enqueue(new Callback<TodoReturningResponse, HasuraException>() {
                    @Override
                    public void onSuccess(TodoReturningResponse response) {
                        hideProgressIndicator();
                        adapter.deleteData(recyclerViewPosition, record);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressIndicator();
                        handleError(e);
                    }
                });
    }

    private void addATodo(final String description) {
        showProgressIndicator();

        client.useDataService()
                .setRequestBody(new InsertTodoRequest(description, user.getId()))
                .expectResponseType(TodoReturningResponse.class)
                .enqueue(new Callback<TodoReturningResponse, HasuraException>() {
                    @Override
                    public void onSuccess(TodoReturningResponse response) {
                        hideProgressIndicator();
                        TodoRecord record = new TodoRecord(description, user.getId(), false);
                        record.setId(response.getTodoRecords().get(0).getId());
                        adapter.addData(record);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        hideProgressIndicator();
                        handleError(e);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTodo:
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                alert.setMessage("Describe your task");
                alert.setTitle("Create new task");
                alert.setView(edittext);
                alert.setPositiveButton("Add Todo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addATodo(edittext.getText().toString());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
                return true;
            case R.id.signOut:
                AlertDialog.Builder signOutAlert = new AlertDialog.Builder(this);
                signOutAlert.setTitle("Sign Out");
                signOutAlert.setMessage("Are you sure you want to sign out?");
                signOutAlert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                signOutAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgressIndicator();
                        user.logout(new LogoutResponseListener() {
                            @Override
                            public void onSuccess(String message) {
                                hideProgressIndicator();
                                completeUserLogout();
                            }

                            @Override
                            public void onFailure(HasuraException e) {
                                hideProgressIndicator();
                                handleError(e);
                            }
                        });
                    }
                });
                signOutAlert.show();
                return true;
        }
        return false;
    }

}
