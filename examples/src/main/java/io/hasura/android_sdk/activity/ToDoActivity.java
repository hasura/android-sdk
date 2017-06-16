package io.hasura.android_sdk.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import io.hasura.android_sdk.R;
import io.hasura.android_sdk.models.DeleteTodoRequest;
import io.hasura.android_sdk.models.InsertTodoRequest;
import io.hasura.android_sdk.models.SelectTodoRequest;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.android_sdk.models.TodoReturningResponse;
import io.hasura.android_sdk.models.UpdateTodoRequest;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraQuery;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.responseListener.LogoutResponseListener;
import io.hasura.sdk.Callback;
import io.hasura.sdk.HasuraException;

public class ToDoActivity extends BaseActivity {

    private static String TAG = "ToDoActivity";

    ToDoRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    HasuraUser user;

    public static void startActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity, ToDoActivity.class));
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        user = HasuraClient.currentUser();
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

    private void fetchTodosFromDB() {
        showProgressIndicator();

        user.useDataService()
                .setRequestBody(new SelectTodoRequest(user.getId()))
                .expectResponseTypeArrayOf(TodoRecord.class)
                .enqueue(new Callback<List<TodoRecord>, HasuraException>() {
                    @Override
                    public void onSuccess(List<TodoRecord> response) {

                        for (TodoRecord record: response) {
                            Log.i("ResponseRecord",record.toString());
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

        HasuraQuery<TodoReturningResponse> updateTodoQuery = user
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

        user.useDataService()
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

        user.useDataService()
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
                            public void onSuccess() {
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
