package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int subject_id;
    public String subject_name;
    public String importance;
    public int ect_points;
    public boolean is_lecture;
    public boolean is_exercise;
    public boolean is_lab;
    public int profile_id;
}
