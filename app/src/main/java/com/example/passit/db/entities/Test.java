package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Test {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int test_id;
    public String test_name;
    public String importance;
    public String date_due;
    public String description;
    @ColumnInfo(defaultValue = "false")
    public boolean finished;
    public int subject_id;
}
