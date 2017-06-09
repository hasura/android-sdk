package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class SelectTodoRequest {

    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    public SelectTodoRequest(Integer userId) {
        args = new Args();
        args.where = new Where();
        args.where.userId = userId;
    }

    class Args {

        @SerializedName("table")
        String table = "todo";

        @SerializedName("columns")
        String[] columns = {
                "id","title","completed"
        };

        @SerializedName("where")
        Where where;

    }

    class Where {
        @SerializedName("user_id")
        Integer userId;
    }

}
