package com.example.passit.rvadapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.R;
import com.example.passit.TaskInfo;
import com.example.passit.TestInfo;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;

import java.util.List;

public class TasksViewRVAdapter extends RecyclerView.Adapter<TasksViewRVAdapter.ViewHolder> {

    private final List<Task> taskList;
    private AppDatabase db;

    public TasksViewRVAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TasksViewRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_task_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewRVAdapter.ViewHolder holder, int position) {

        db = AppDatabase.getDbInstance(holder.subjectName.getContext());
        int taskId = taskList.get(position).getTask_id();

        holder.subjectName.setText(db.profileDao().getSubjectName(taskList.get(position).getSubject_id()));
        holder.taskName.setText(taskList.get(position).getTask_name());
        holder.dateTV.setText(taskList.get(position).getDate_due());
        holder.timeTV.setText(taskList.get(position).getHour_due());

        if (taskList.get(position).getImportance().equals("normal")) {
            holder.rowLayout.setBackgroundResource(R.drawable.normal_importance_gradient);
            //holder.subjectName.setTextColor(ContextCompat.getColor(this, R.color.background));
        } else if (taskList.get(position).getImportance().equals("medium")) {
            holder.rowLayout.setBackgroundResource(R.drawable.medium_importance_gradient);
        } else if (taskList.get(position).getImportance().equals("high")) {
            holder.rowLayout.setBackgroundResource(R.drawable.high_importance_gradient);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), TaskInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("taskId", taskId);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectName, taskName, dateTV, timeTV;
        private final ConstraintLayout rowLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            taskName = itemView.findViewById(R.id.taskName);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            rowLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
