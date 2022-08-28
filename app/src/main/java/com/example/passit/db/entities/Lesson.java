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
    public int subject_id;

    public int getLesson_id() {
        return lesson_id;
    }

    public String getLesson_type() {
        return lesson_type;
    }

    public String getLecturer_name() {
        return lecturer_name;
    }

    public boolean isIs_even_lesson() {
        return is_even_lesson;
    }

    public int getStarting_week() {
        return starting_week;
    }

    public int getEnding_week() {
        return ending_week;
    }

    public boolean isPassed() {
        return passed;
    }

    public int getSubject_id() {
        return subject_id;
    }
}
