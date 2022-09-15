package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.passit.db.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskInfo extends AppCompatActivity {

    private int taskId;
    private TextView testNameTV, assignedSubjectTV, dateTV, testDescriptionTV, importanceTV;
    private AppDatabase db;
    private List<Task> tasksList = new ArrayList<>();
    private Button deleteBtn, editBtn, finishBtn, returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskId = extras.getInt("taskId");
        }

        importanceTV = findViewById(R.id.importanceTV);
        testNameTV = findViewById(R.id.noteTitle);
        assignedSubjectTV = findViewById(R.id.assignedSubjectTV);
        dateTV = findViewById(R.id.dateTV);
        testDescriptionTV = findViewById(R.id.noteDescription);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);
        finishBtn = findViewById(R.id.markFinishedBtn);
        returnBtn = findViewById(R.id.returnBtn);

        db = AppDatabase.getDbInstance(this);

        tasksList = db.profileDao().getTaskWithId(taskId);

        switch (tasksList.get(0).getImportance()) {
            case "normal":
                importanceTV.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                importanceTV.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                importanceTV.setBackgroundResource(R.color.highImportance);
                break;
        }

        if (db.profileDao().getTaskState(taskId)) {
            finishBtn.setBackgroundResource(R.drawable.finish_button);
        } else {
            finishBtn.setBackgroundResource(R.drawable.unfinished_button);
        }

        setText();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.profileDao().deleteTask(taskId);
                returnToView();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (db.profileDao().getTaskState(taskId)) {
                    finishBtn.setBackgroundResource(R.drawable.unfinished_button);
                    db.profileDao().setUnfinishedTask(taskId);
                } else {
                    finishBtn.setBackgroundResource(R.drawable.finish_button);
                    db.profileDao().setFinishedTask(taskId);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTask.class);
                Bundle bundle = new Bundle();
                bundle.putInt("taskId", taskId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToView();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void setText() {
        String dateWithHour = tasksList.get(0).getDate_due() + "  " + tasksList.get(0).getHour_due();

        testNameTV.setText(tasksList.get(0).getTask_name());
        assignedSubjectTV.setText(db.profileDao().getSubjectName(tasksList.get(0).getSubject_id()) + "\n"
                + tasksList.get(0).getSubject_type());
        dateTV.setText("Termin:  " + dateWithHour);
        testDescriptionTV.setText(tasksList.get(0).getDescription());
    }

    @Override
    public void onBackPressed() {
        returnToView();
    }

    public void returnToView() {
        Intent intent = new Intent(this, TasksView.class);
        startActivity(intent);
    }
}