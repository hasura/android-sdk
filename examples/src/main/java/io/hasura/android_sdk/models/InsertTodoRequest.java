package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaison on 23/01/17.
 */

public class InsertTodoRequest {

    @SerializedName("type")
    String type = "insert";

    @SerializedName("args")
    Args args;

    public InsertTodoRequest(String title, Integer userId) {
        args = new Args();
        args.objects = new ArrayList<>();
        TodoRecord record = new TodoRecord(title,userId, false);
        args.objects.add(record);
    }

    class Args {

        @SerializedName("table")
        String table = "todo";

        @SerializedName("returning")
        String[] returning = {
                "id","title","completed"
        };

        @SerializedName("objects")
        List<TodoRecord> objects;

    }

}
