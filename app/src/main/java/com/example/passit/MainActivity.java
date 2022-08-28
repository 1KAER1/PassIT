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

    private ImageButton tasksButton, addSubjectButton, testsButton;
    private long pressedTime;
    private List<Profile> profilesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addSubjectButton = findViewById(R.id.subjectsBtn);
        tasksButton = findViewById(R.id.tasksViewBtn);
        testsButton = findViewById(R.id.testsButton);

        AppDatabase db = AppDatabase.getDbInstance(this);

        profilesList = db.profileDao().getAllProfiles();

        if (profilesList.isEmpty()) {
            addNewProfile();
        }

        addSubjectButton.setOnClickListener(view -> openSubjects());
        tasksButton.setOnClickListener(view -> openTasksView());
        testsButton.setOnClickListener(view -> openTestsView());

    }

    public void openSubjects() {
        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }

    public void addNewProfile() {
        Intent intent = new Intent(this, AddNewProfile.class);
        startActivity(intent);
    }

    public void openTasksView() {
        Intent intent = new Intent(this, TasksView.class);
        startActivity(intent);
    }

    public void openTestsView() {
        Intent intent = new Intent(this, TestsView.class);
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