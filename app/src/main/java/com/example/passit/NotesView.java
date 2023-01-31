package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.passit.db.AppDatabase;
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

        setTitle("Notatki");

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