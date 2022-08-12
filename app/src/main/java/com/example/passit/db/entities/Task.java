package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int task_id;
    public String task_name;
    public String importance;
    public String date_due;
    public String description;
    @ColumnInfo(defaultValue = "false")
    public boolean finished;
    public int subject_id;
}
