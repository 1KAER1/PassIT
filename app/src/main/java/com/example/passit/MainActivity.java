package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.passit.db.entities.Profile;

public class MainActivity extends AppCompatActivity {

    private Button addSubjectButton, scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        addSubjectButton = findViewById(R.id.subjectsBtn);
        scheduleButton = findViewById(R.id.scheduleBtn);

        addSubjectButton.setOnClickListener(view -> openSubjects());
        scheduleButton.setOnClickListener(view -> addNewSubject());

    }

    public void openSubjects() {
        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }

    public void addNewSubject() {
        Intent intent = new Intent(this, SubjectInformation.class);
        startActivity(intent);
    }

}