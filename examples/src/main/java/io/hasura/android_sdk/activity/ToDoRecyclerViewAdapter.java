package io.hasura.android_sdk.activity;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import io.hasura.android_sdk.R;
import io.hasura.android_sdk.models.TodoRecord;
import io.hasura.android_sdk.models.TodoReturningResponse;

/**
 * Created by jaison on 11/01/17.
 */

public class ToDoRecyclerViewAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    List<TodoRecord> data = new ArrayList<>();

    public interface Interactor {
        void onTodoClicked(int position, TodoRecord record);
        void onTodoLongClicked(int position, TodoRecord record);
    }

    Interactor interactor;

    public ToDoRecyclerViewAdapter(Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_todo,parent,false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, final int position) {
        final TodoRecord todoRecord = data.get(position);
        holder.description.setText(todoRecord.getTitle());
        holder.checkbox.setChecked(todoRecord.getCompleted());

        if(todoRecord.getCompleted()){
            holder.description.setPaintFlags(holder.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.description.setPaintFlags(holder.description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interactor.onTodoLongClicked(position,todoRecord);
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactor.onTodoClicked(position,todoRecord);
            }
        });

        holder.checkbox.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<TodoRecord> recordList) {
        this.data = recordList;
        notifyDataSetChanged();
    }

    public void deleteData(int position, TodoRecord record) {
        this.data.remove(position);
        notifyDataSetChanged();
    }

    public void updateData(int position, TodoRecord record) {
        this.data.set(position,record);
        notifyDataSetChanged();
    }

    public void addData(TodoRecord record) {
        this.data.add(record);
        notifyDataSetChanged();
    }
}
