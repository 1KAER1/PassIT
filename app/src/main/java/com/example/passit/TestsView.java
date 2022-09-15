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
import com.example.passit.db.entities.Test;
import com.example.passit.rvadapters.TestsViewRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    Button addNewTest;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private TestsViewRVAdapter adapter;
    private List<Test> testNameList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();
    private AppDatabase db;
    private String selectedImportance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_view);

        addNewTest = findViewById(R.id.addNewNote);
        recyclerView = findViewById(R.id.tasksRV);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        searchBar = findViewById(R.id.searchBar);

        db = AppDatabase.getDbInstance(this);

        testNameList = db.profileDao().getAllTests();
        subjectList = db.profileDao().getAllSubjects();

        selectedImportance = checkImportanceSelection();

        adapter = new TestsViewRVAdapter(testNameList);
        recyclerView.setAdapter(adapter);

        addNewTest.setOnClickListener(view -> {
            if (subjectList.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Dodaj przedmioty, aby móc dodawać zaliczenia!",
                        Toast.LENGTH_SHORT).show();
            } else {
                openAddNewTest();
            }
        });
    }

    public String checkImportanceSelection() {

        String selectedImportance;
        if (normalImportance.isChecked()) {
            selectedImportance = "normal";
        } else if (mediumImportance.isChecked()) {
            selectedImportance = "medium";
        } else if (highImportance.isChecked()) {
            selectedImportance = "high";
        } else {
            selectedImportance = null;
        }
        return selectedImportance;
    }

    public void openAddNewTest() {
        Intent intent = new Intent(this, AddTest.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}