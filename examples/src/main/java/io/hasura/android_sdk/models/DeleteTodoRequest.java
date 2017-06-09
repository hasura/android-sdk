package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaison on 23/01/17.
 */

public class DeleteTodoRequest {

    @SerializedName("type")
    String type = "delete";

    @SerializedName("args")
    Args args;

    public DeleteTodoRequest(Integer todoId, Integer userId) {
        args = new Args();
        args.where = new Where();
        args.where.id = todoId;
        args.where.userId = userId;
    }

    class Args {

        @SerializedName("table")
        String table = "todo";

        @SerializedName("where")
        Where where;
    }

    class Where {
        @SerializedName("user_id")
        Integer userId;

        @SerializedName("id")
        Integer id;
    }
}
