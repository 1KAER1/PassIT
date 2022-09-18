package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        String userName = userNameET.getText().toString();

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