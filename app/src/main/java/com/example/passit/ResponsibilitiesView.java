package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.rvadapters.ResponsibilitiesRVAdapter;
import com.example.passit.rvadapters.TasksViewRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ResponsibilitiesView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private FloatingActionButton addNewBtn;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private ResponsibilitiesRVAdapter adapter;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibilities_view);

        addNewBtn = findViewById(R.id.addNewBtn);
        recyclerView = findViewById(R.id.responsibilitiesRV);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        searchBar = findViewById(R.id.searchBar);

        db = AppDatabase.getDbInstance(this);

        responsibilitiesList = db.profileDao().getAllResponsibilities();
        subjectList = db.profileDao().getAllSubjects();

        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
        recyclerView.setAdapter(adapter);

        addNewBtn.setOnClickListener(view -> {
            if (subjectList.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Dodaj przedmioty, aby móc dodawać zadania!",
                        Toast.LENGTH_SHORT).show();
            } else {
                openAddNewTask();
            }
        });
    }

    public void openAddNewTask() {
        Intent intent = new Intent(this, AddResponsibility.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}