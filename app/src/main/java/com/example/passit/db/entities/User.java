package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int user_id;
    public String user_name;

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }
}
