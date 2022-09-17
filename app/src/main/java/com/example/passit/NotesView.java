package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.passit.db.entities.Note;
import com.example.passit.rvadapters.NotesViewRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesView extends AppCompatActivity {

    private FloatingActionButton addNewNoteBtn;
    private AppDatabase db;
    private List<Note> noteList = new ArrayList<>();
    private NotesViewRVAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        db = AppDatabase.getDbInstance(this);

        addNewNoteBtn = findViewById(R.id.addNewBtn);
        recyclerView = findViewById(R.id.responsibilitiesRV);

        noteList = db.profileDao().getAllNotes();

        adapter = new NotesViewRVAdapter(noteList);
        recyclerView.setAdapter(adapter);

        addNewNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();
            }
        });
    }

    public void addNewNote() {
        Intent intent = new Intent(this, NoteInfo.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}