package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioButton;

import com.example.passit.db.AppDatabase;
import com.example.passit.db.entities.Subject;
import com.example.passit.rvadapters.SubjectsViewRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SubjectsView extends AppCompatActivity {

    private RecyclerView recyclerView;
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

        setTitle("Przedmioty");

        recyclerView = findViewById(R.id.subjectsRV);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
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