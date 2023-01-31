package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.passit.db.AppDatabase;
import com.example.passit.db.entities.Profile;
import com.example.passit.helpers.InputFilterMinMax;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddNewProfile extends AppCompatActivity {

    private TextInputEditText profileNameET, semesterET;
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

        semesterET.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});

        addProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfileToDb();
            }
        });

        profileNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        semesterET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        String profileName = profileNameET.getText().toString().replaceAll("\n", " ").replaceAll(" +", " ").trim();

        Profile profile = new Profile();
        profile.profile_name = profileName;
        profile.semester = Integer.parseInt(semesterET.getText().toString());
        profile.user_id = db.profileDao().getLastUserId();
        db.profileDao().deactivateProfiles();
        db.profileDao().insertProfile(profile);
        db.profileDao().activateProfile(profileName.trim());

        returnToMainMenu();
    }

    public void returnToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}