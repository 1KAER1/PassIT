package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.rvadapters.SubjectsViewRVAdapter;
import com.example.passit.rvadapters.TasksViewRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class TasksView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    Button addNewTask;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private TasksViewRVAdapter adapter;
    private List<Task> tasksNameList = new ArrayList<>();
    Button addNewTaskButton;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_view);

        addNewTask = findViewById(R.id.addNewTask);
        recyclerView = findViewById(R.id.tasksRV);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        searchBar = findViewById(R.id.searchBar);

        db = AppDatabase.getDbInstance(this);

        tasksNameList = db.profileDao().getAllTasks();

        adapter = new TasksViewRVAdapter(tasksNameList);
        recyclerView.setAdapter(adapter);

        addNewTask.setOnClickListener(view -> openAddNewTask());

    }

    public void openAddNewTask() {
        Intent intent = new Intent(this, AddTask.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}