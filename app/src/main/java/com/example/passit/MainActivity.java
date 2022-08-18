package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;

public class MainActivity extends AppCompatActivity {

    private Button addSubjectButton, scheduleButton, tasksButton, testsButton;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addSubjectButton = findViewById(R.id.subjectsBtn);
        scheduleButton = findViewById(R.id.scheduleBtn);
        tasksButton = findViewById(R.id.openTasksBtn);
        testsButton = findViewById(R.id.testsButton);

        AppDatabase db = AppDatabase.getDbInstance(this);

        /*Profile profile = new Profile();
        profile.profile_name = "Informatyka";
        profile.semester = 7;
        db.profileDao().insertProfile(profile);*/

        addSubjectButton.setOnClickListener(view -> openSubjects());
        scheduleButton.setOnClickListener(view -> addNewSubject());
        tasksButton.setOnClickListener(view -> openTasksView());
        testsButton.setOnClickListener(view -> openTestsView());

    }

    public void openSubjects() {
        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }

    public void addNewSubject() {
        Intent intent = new Intent(this, SubjectInformation.class);
        startActivity(intent);
    }

    public void openTasksView(){
        Intent intent = new Intent(this, TasksView.class);
        startActivity(intent);
    }

    public void openTestsView(){
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