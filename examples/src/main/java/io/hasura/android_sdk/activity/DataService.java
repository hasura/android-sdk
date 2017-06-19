package io.hasura.android_sdk.activity;

import io.hasura.android_sdk.models.SelectTodoRequest;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.sdk.query.HasuraQuery;

/**
 * Created by jaison on 15/06/17.
 */

public interface DataService {
    HasuraQuery<TodoRecord> getTodo(SelectTodoRequest request);
}
