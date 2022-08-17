package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.passit.db.entities.Subject;
import com.example.passit.rvadapters.SubjectsViewRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubjectsView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private SubjectsViewRVAdapter adapter;
    private List<Subject> subjectNameList = new ArrayList<>();
    AppDatabase db;


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

        db = AppDatabase.getDbInstance(getApplicationContext());

        subjectNameList = db.profileDao().getAllSubjects();

        adapter = new SubjectsViewRVAdapter(subjectNameList);
        recyclerView.setAdapter(adapter);


    }
}