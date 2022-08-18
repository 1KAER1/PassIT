package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.passit.db.entities.Test;

import java.util.ArrayList;
import java.util.List;

public class TestsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    Button addNewTest;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private TestsViewRVAdapter adapter;
    private List<Test> testNameList = new ArrayList<>();
    Button addNewTaskButton;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_view);

        addNewTest = findViewById(R.id.addNewTask);
        recyclerView = findViewById(R.id.tasksRV);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        searchBar = findViewById(R.id.searchBar);

        db = AppDatabase.getDbInstance(this);

        testNameList = db.profileDao().getAllTests();

        adapter = new TestsViewRVAdapter(testNameList);
        recyclerView.setAdapter(adapter);

        addNewTest.setOnClickListener(view -> openAddNewTest());
    }

    public void openAddNewTest() {
        Intent intent = new Intent(this, AddTest.class);
        startActivity(intent);
    }
}