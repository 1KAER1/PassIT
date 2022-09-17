package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.passit.db.entities.Subject;
import com.example.passit.rvadapters.SubjectsViewRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SubjectsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private FloatingActionButton addNewSubject;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private SubjectsViewRVAdapter adapter;
    private List<Subject> subjectNameList = new ArrayList<>();
    private AppDatabase db;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_view);

        recyclerView = findViewById(R.id.subjectsRV);
        searchBar = findViewById(R.id.searchBar);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        addNewSubject = findViewById(R.id.addNewSubject);

        db = AppDatabase.getDbInstance(this);

        subjectNameList = db.profileDao().getAllSubjects();

        adapter = new SubjectsViewRVAdapter(subjectNameList);
        recyclerView.setAdapter(adapter);

        addNewSubject.setOnClickListener(view -> addNewSubject());

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addNewSubject() {
        Intent intent = new Intent(this, AddSubject.class);
        startActivity(intent);
    }
}