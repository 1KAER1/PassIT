package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;

import java.util.ArrayList;
import java.util.List;

public class AddNewProfile extends AppCompatActivity {

    private EditText profileNameET, semesterET;
    private Button addProfileBtn;
    private AppDatabase db;
    private List<String> profileNamesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profile);

        profileNameET = findViewById(R.id.noteTitle);
        semesterET = findViewById(R.id.semesterET);
        addProfileBtn = findViewById(R.id.addProfileBtn);
        db = AppDatabase.getDbInstance(this);

        addProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfileToDb();
            }
        });

    }

    public void addProfileToDb() {
        if (checkInput() && checkProfileName()) {
            addDatabaseEntry();
        } else {
            Toast.makeText(this, "Uzupełnij wszystkie dane!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkInput() {
        return !profileNameET.getText().toString().isEmpty()
                && !semesterET.getText().toString().isEmpty();
    }

    public boolean checkProfileName() {
        profileNamesList = db.profileDao().getAllProfilesNames();

        if (profileNamesList.contains(profileNameET.getText().toString())) {
            Toast.makeText(this, "Profil o takiej nazwie już istnieje!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void addDatabaseEntry() {

        String profileName = profileNameET.getText().toString();

        Profile profile = new Profile();
        profile.profile_name = profileName;
        profile.semester = Integer.parseInt(semesterET.getText().toString());
        db.profileDao().deactivateProfiles();
        db.profileDao().insertProfile(profile);
        db.profileDao().activateProfile(profileName);

        returnToMainMenu();
    }

    public void returnToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}