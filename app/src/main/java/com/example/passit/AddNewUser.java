package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.User;
import com.google.android.material.textfield.TextInputEditText;

public class AddNewUser extends AppCompatActivity {

    private Button addUserBtn;
    private TextInputEditText userNameET;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        db = AppDatabase.getDbInstance(this);

        userNameET = findViewById(R.id.userName);
        addUserBtn = findViewById(R.id.addUserBtn);


        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserToDb();
            }
        });

        userNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    public void addUserToDb() {
        if (checkInput()) {
            addDatabaseEntry();
        } else {
            Toast.makeText(this, "Podaj swoje imię lub pseudonim!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkInput() {
        return !userNameET.getText().toString().isEmpty();
    }

    public void addDatabaseEntry() {

        String userName = userNameET.getText().toString().replaceAll("\n", " ").replaceAll(" +", " ").trim();

        User user = new User();
        user.user_name = userName;
        db.profileDao().insertUser(user);

        addNewProfile();
    }

    public void addNewProfile() {
        Intent intent = new Intent(this, AddNewProfile.class);
        startActivity(intent);
    }
}