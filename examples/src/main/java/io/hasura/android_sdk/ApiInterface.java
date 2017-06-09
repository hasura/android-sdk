package io.hasura.android_sdk;

import java.util.List;

import io.hasura.android_sdk.models.SelectTodoRequest;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.android_sdk.models.TodoReturningResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by jaison on 09/06/17.
 */

public interface ApiInterface {

    @GET("ads")
    Call<List<TodoRecord>> getTodos(@Body SelectTodoRequest request);
}
