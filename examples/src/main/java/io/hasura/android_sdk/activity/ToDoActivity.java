package io.hasura.android_sdk.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import io.hasura.android_sdk.R;
import io.hasura.android_sdk.models.MessageResponse;
import io.hasura.android_sdk.models.SelectTodoQuery;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.sdk.auth.HasuraException;
import io.hasura.sdk.core.Callback;
import io.hasura.sdk.utils.Hasura;
import io.hasura.sdk.utils.HasuraSessionStore;
import okhttp3.ResponseBody;


public class ToDoActivity extends BaseActivity {

    private static String TAG = "ToDoActivity";

    ToDoRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public static void startActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity,ToDoActivity.class));
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_to_do);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        adapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.Interactor() {
//            @Override
//            public void onTodoClicked(int position, TodoRecord record) {
//                toggleTodo(position, record);
//            }
//
//            @Override
//            public void onTodoLongClicked(int position, TodoRecord record) {
//                deleteTodo(position, record);
//            }
//        });
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//        fetchTodosFromDB();
    }

//    private void handleError(ResponseBody error) {
//        try {
//            MessageResponse messageResponse = new Gson().fromJson(error.string(), MessageResponse.class);
//            showErrorAlert(messageResponse.getMessage(),null);
//            if (messageResponse.getMessage().contentEquals("invalid authorization token"))
//                completeUserLogout();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void fetchTodosFromDB() {
//        showProgressIndicator();
//
//
////        Hasura.db.getTodos(new SelectTodoQuery(Hasura.getUserId())).enqueue(new Callback<List<TodoRecord>>() {
////            @Override
////            public void onResponse(Call<List<TodoRecord>> call, Response<List<TodoRecord>> response) {
////                hideProgressIndicator();
////                if (response.isSuccessful()) {
////                    adapter.setData(response.body());
////                } else {
////                    handleError(response.errorBody());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<TodoRecord>> call, Throwable t) {
////                hideProgressIndicator();
////                showErrorAlert("Something went wrong. Please ensure that you have a working internet connection",null);
////            }
////        });
//    }
//
//    private void toggleTodo(final int recyclerViewPostion, final TodoRecord record) {
//        showProgressIndicator();
//        record.setCompleted(!record.getCompleted());
//        UpdateTodoQuery query = new UpdateTodoQuery(record.getId(), Hasura.getUserId(), record.getTitle(), record.getCompleted());
//        Hasura.db.updateATodo(query).enqueue(new Callback<TodoReturningResponse>() {
//            @Override
//            public void onResponse(Call<TodoReturningResponse> call, Response<TodoReturningResponse> response) {
//                hideProgressIndicator();
//                if (response.isSuccessful()) {
//                    adapter.updateData(recyclerViewPostion,record);
//                } else {
//                    handleError(response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TodoReturningResponse> call, Throwable t) {
//                hideProgressIndicator();
//                showErrorAlert("Something went wrong. Please ensure that you have a working internet connection",null);
//            }
//        });
//
//    }
//
//    private void deleteTodo(final int recyclerViewPosition, final TodoRecord record) {
//        showProgressIndicator();
//        Hasura.db.deleteATodo(new DeleteTodoQuery(record.getId(),Hasura.getUserId())).enqueue(new Callback<TodoReturningResponse>() {
//            @Override
//            public void onResponse(Call<TodoReturningResponse> call, Response<TodoReturningResponse> response) {
//                hideProgressIndicator();
//                if (response.isSuccessful()) {
//                    adapter.deleteData(recyclerViewPosition,record);
//                } else {
//                    handleError(response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TodoReturningResponse> call, Throwable t) {
//                hideProgressIndicator();
//                showErrorAlert("Please ensure that you have a working internet connection",null);
//            }
//        });
//    }
//
//    private void addATodo(final String description) {
//        showProgressIndicator();
//        Hasura.db.addATodo(new InsertTodoQuery(description,Hasura.getUserId())).enqueue(new Callback<TodoReturningResponse>() {
//            @Override
//            public void onResponse(Call<TodoReturningResponse> call, Response<TodoReturningResponse> response) {
//                hideProgressIndicator();
//                if (response.isSuccessful()) {
//                    TodoRecord record = new TodoRecord(description,Hasura.getUserId(),false);
//                    record.setId(response.body().getTodoRecords().get(0).getId());
//                    adapter.addData(record);
//                } else {
//                    handleError(response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TodoReturningResponse> call, Throwable t) {
//                hideProgressIndicator();
//                showErrorAlert("PLease ensure that you have a working internet connection",null);
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.addTodo:
//                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//                final EditText edittext = new EditText(this);
//                alert.setMessage("Describe your task");
//                alert.setTitle("Create new task");
//                alert.setView(edittext);
//                alert.setPositiveButton("Add Todo", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        addATodo(edittext.getText().toString());
//                    }
//                });
//
//                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                    }
//                });
//
//                alert.show();
//                return true;
//            case R.id.signOut:
//                AlertDialog.Builder signOutAlert = new AlertDialog.Builder(this);
//                signOutAlert.setTitle("Sign Out");
//                signOutAlert.setMessage("Are you sure you want to sign out?");
//                signOutAlert.setNeutralButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                signOutAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        showProgressIndicator();
//                        Hasura.auth.logout().enqueue(new Callback<MessageResponse>() {
//                            @Override
//                            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
//                                hideProgressIndicator();
//                                if (response.isSuccessful()) {
//                                    completeUserLogout();
//                                } else {
//                                    handleError(response.errorBody());
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<MessageResponse> call, Throwable t) {
//                                hideProgressIndicator();
//                                showErrorAlert("Please ensure that you have a working internet connection",null);
//                            }
//                        });
//                    }
//                });
//                signOutAlert.show();
//                return true;
//        }
//        return false;
//    }
//
//    private void completeUserLogout() {
//        Hasura.clearSession();
//        AuthenticationActivity.startActivity(ToDoActivity.this);
//    }
}
