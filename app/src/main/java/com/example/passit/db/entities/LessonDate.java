package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LessonDate {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int lesson_date_id;
    public String day_name;
    public String lesson_time;
    public int lesson_period;
    public int lesson_id;

    public int getLesson_date_id() {
        return lesson_date_id;
    }

    public String getDay_name() {
        return day_name;
    }

    public String getLesson_time() {
        return lesson_time;
    }

    public int getLesson_period() {
        return lesson_period;
    }

    public int getLesson_id() {
        return lesson_id;
    }
}
