package io.hasura.android_sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaison on 23/01/17.
 */

public class TodoRecord {

    @SerializedName("id")
    Integer id;

    @SerializedName("title")
    String title;

    @SerializedName("user_id")
    Integer userId;

    @SerializedName("completed")
    Boolean completed;

    public TodoRecord(String title, Integer userId, Boolean completed) {
        this.title = title;
        this.userId = userId;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
