package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton addSubjectButton, respButton, notesButton, calendarButton;
    private long pressedTime;
    private List<Profile> profilesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addSubjectButton = findViewById(R.id.subjectsBtn);
        respButton = findViewById(R.id.respButton);
        notesButton = findViewById(R.id.notesViewBtn);
        calendarButton = findViewById(R.id.calendarViewBtn);

        AppDatabase db = AppDatabase.getDbInstance(this);

        profilesList = db.profileDao().getAllProfiles();

        if (profilesList.isEmpty()) {
            addNewProfile();
        }

        addSubjectButton.setOnClickListener(view -> openView(SubjectsView.class));
        respButton.setOnClickListener(view -> openView(ResponsibilitiesView.class));
        notesButton.setOnClickListener(view -> openView(NotesView.class));
        calendarButton.setOnClickListener(view -> openView(CalendarActivity.class));

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