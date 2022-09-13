package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TasksViewRVAdapter.ViewHolder holder, int position) {

        db = AppDatabase.getDbInstance(holder.taskName.getContext());
        int taskId = taskList.get(position).getTask_id();

        //holder.subjectName.setText(db.profileDao().getSubjectName(taskList.get(position).getSubject_id()));
        holder.taskName.setText(taskList.get(position).getTask_name() + "\n\n" + db.profileDao().getSubjectName(taskList.get(position).getSubject_id()));
        holder.rowLayout.setBackgroundResource(R.color.cardBackground);

        if (db.profileDao().getTaskState(taskId)) {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
            //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.rowLayout.setBackgroundResource(R.color.cardBackgroundFinished);
        } else {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
            //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.rowLayout.setBackgroundResource(R.color.cardBackground);
        }



        switch (taskList.get(position).getImportance()) {
            case "normal":
                holder.importanceLabel.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                holder.importanceLabel.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                holder.importanceLabel.setBackgroundResource(R.color.highImportance);
                break;
        }

        holder.markFinishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.profileDao().getTaskState(taskId)) {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
                    //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.rowLayout.setBackgroundResource(R.color.cardBackground);
                    db.profileDao().setUnfinishedTask(taskId);
                } else {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
                    //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.rowLayout.setBackgroundResource(R.color.cardBackgroundFinished);
                    db.profileDao().setFinishedTask(taskId);
                }
            }
        });

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
        private final TextView taskName;
        private final ConstraintLayout rowLayout;
        private final Button markFinishedBtn;
        private final ImageView importanceLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            markFinishedBtn = itemView.findViewById(R.id.markFinished);
            rowLayout = itemView.findViewById(R.id.rowLayout);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
        }
    }
}
