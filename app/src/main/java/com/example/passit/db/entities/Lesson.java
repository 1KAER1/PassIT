package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int lesson_id;
    public String lesson_type;
    public String lecturer_name;
    @Nullable
    public boolean is_even_lesson;
    public int starting_week;
    public int ending_week;
    @ColumnInfo(defaultValue = "false")
    public boolean passed;
    public int lesson_date_id;
    public int subject_id;

}
