package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaison on 23/01/17.
 */

public class HasuraQuery {

    @SerializedName("type")
    String type;

    class Args {

        @SerializedName("table")
        String table;

        @SerializedName("$set")
        TodoRecord set;

        @SerializedName("objects")
        List<TodoRecord> objects;

        @SerializedName("returning")
        String[] returning = {
                "id","title","completed"
        };


    }
}
