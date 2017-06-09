package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class UpdateTodoRequest {

    @SerializedName("type")
    String type = "update";

    @SerializedName("args")
    Args args;

    public UpdateTodoRequest(Integer todoId, Integer userId, String title, Boolean completed) {
        args = new Args();
        args.where = new Where();
        args.where.id = todoId;
        args.where.userId = userId;
        args.set = new Set();
        args.set.title = title;
        args.set.completed = completed;
    }

    class Args {

        @SerializedName("table")
        String table = "todo";

        @SerializedName("where")
        Where where;

        @SerializedName("$set")
        Set set;

        @SerializedName("returning")
        String[]  returning = {
                "id","completed","title"
        };
    }

    class Where {
        @SerializedName("user_id")
        Integer userId;

        @SerializedName("id")
        Integer id;
    }

    class Set {
        @SerializedName("title")
        String title;

        @SerializedName("completed")
        Boolean completed;
    }
}
