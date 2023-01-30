package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passit.db.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteInfo extends AppCompatActivity {

    private AppDatabase db;
    private int noteId;
    private EditText noteTitle, noteDescription;
    private Button checkBtn, backBtn;
    private long pressedTime;
    private boolean isEdit = false;
    private boolean isAdd = true;
    private List<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = AppDatabase.getDbInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdd = false;
            noteId = extras.getInt("noteId");
            noteList = db.profileDao().getNoteWithId(noteId);
        }

        noteTitle = findViewById(R.id.noteTitle);
        noteDescription = findViewById(R.id.noteDescription);
        checkBtn = findViewById(R.id.checkBtn);
        backBtn = findViewById(R.id.backBtn);

        if (isAdd) {
            checkBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
            backBtn.setBackgroundResource(R.drawable.ic_clear_24);
        } else {
            noteTitle.setEnabled(false);
            noteDescription.setEnabled(false);
            noteTitle.setText(noteList.get(0).getNote_title());
            noteDescription.setText(noteList.get(0).getNote_description());
        }

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    addNoteToDatabase();

                } else if (isEdit) {
                    updateNote();
                    isEdit = false;
                    noteTitle.setEnabled(false);
                    noteDescription.setEnabled(false);
                    checkBtn.setBackgroundResource(R.drawable.ic_edit_32);
                    backBtn.setBackgroundResource(R.drawable.ic_arrow_back_24);
                } else {
                    isEdit = true;
                    noteTitle.setEnabled(true);
                    noteDescription.setEnabled(true);
                    checkBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
                    backBtn.setBackgroundResource(R.drawable.ic_clear_24);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    checkBtn.setBackgroundResource(R.drawable.ic_edit_32);
                    backBtn.setBackgroundResource(R.drawable.ic_arrow_back_24);
                    noteTitle.setEnabled(false);
                    noteDescription.setEnabled(false);
                    isEdit = false;
                } else {
                    returnToView();
                }
            }
        });

        noteTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        noteDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addNoteToDatabase() {
        if (checkInput()) {
            Note note = new Note();
            note.note_title = noteTitle.getText().toString().trim();
            note.note_description = noteDescription.getText().toString().trim();
            note.profile_id = db.profileDao().getActiveProfile();
            db.profileDao().insertNote(note);
            returnToView();
        } else {
            Toast.makeText(this, "Podaj tytuł notatki!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNote() {
        if (checkInput()) {
            db.profileDao().updateNote(
                    noteTitle.getText().toString().trim(),
                    noteDescription.getText().toString().trim(),
                    noteId);
        } else {
            Toast.makeText(this, "Podaj tytuł notatki!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkInput() {
        return !noteTitle.getText().toString().isEmpty();
    }


    @Override
    public void onBackPressed() {
        if (isAdd || isEdit) {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                returnToView();
            } else {
                Toast.makeText(this, "Kliknij jeszcze raz, aby opuścić. Dane nie zostaną zapisane.", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        } else {
            returnToView();
        }
    }

    public void returnToView() {
        Intent intent = new Intent(this, NotesView.class);
        startActivity(intent);
    }
}