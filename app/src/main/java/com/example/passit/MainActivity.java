package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton tasksButton, addSubjectButton, testsButton, notesButton;
    private long pressedTime;
    private List<Profile> profilesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addSubjectButton = findViewById(R.id.subjectsBtn);
        tasksButton = findViewById(R.id.tasksViewBtn);
        testsButton = findViewById(R.id.testsButton);
        notesButton = findViewById(R.id.notesViewBtn);

        AppDatabase db = AppDatabase.getDbInstance(this);

        profilesList = db.profileDao().getAllProfiles();

        if (profilesList.isEmpty()) {
            addNewProfile();
        }

        addSubjectButton.setOnClickListener(view -> openView(SubjectsView.class));
        tasksButton.setOnClickListener(view -> openView(TasksView.class));
        testsButton.setOnClickListener(view -> openView(TestsView.class));
        notesButton.setOnClickListener(view -> openView(NotesView.class));

    }

    public void addNewProfile() {
        Intent intent = new Intent(this, AddNewProfile.class);
        startActivity(intent);
    }

    public void openView(Class<?> viewClass) {
        Intent intent = new Intent(this, viewClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            this.finishAffinity();
        } else {
            Toast.makeText(this, "Kliknij jeszcze raz, aby zamknąć", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}