package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.passit.db.entities.Task;
import com.example.passit.db.entities.Test;

import java.util.ArrayList;
import java.util.List;

public class TaskInfo extends AppCompatActivity {

    private int taskId;
    private TextView testNameTV, assignedSubjectTV, dateTV, testDescriptionTV, importanceTV;
    private AppDatabase db;
    private List<Task> tasksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskId = extras.getInt("taskId");
        }

        importanceTV = findViewById(R.id.importanceTV);
        testNameTV = findViewById(R.id.testNameTV);
        assignedSubjectTV = findViewById(R.id.assignedSubjectTV);
        dateTV = findViewById(R.id.dateTV);
        testDescriptionTV = findViewById(R.id.testDescription);

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

        String dateWithHour = tasksList.get(0).getDate_due() + "    " + tasksList.get(0).getHour_due();

        testNameTV.setText(tasksList.get(0).getTask_name());
        assignedSubjectTV.setText(db.profileDao().getSubjectName(tasksList.get(0).getSubject_id()));
        dateTV.setText(dateWithHour);
        testDescriptionTV.setText(tasksList.get(0).getDescription());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TasksView.class);
        startActivity(intent);
    }
}