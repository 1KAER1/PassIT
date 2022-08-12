package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int profile_id;
    public String profile_name;
    public int semester;
}
