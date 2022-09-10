package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {
                "subject_name"
        }, unique = true)
})
public class Subject {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int subject_id;
    public String subject_name;
    public String importance;
    public int ects_points;
    public boolean is_lecture;
    public boolean is_exercise;
    public boolean is_lab;
    @Nullable
    @ColumnInfo(defaultValue = "false")
    public boolean passed;
    public int profile_id;

    public int getSubject_id() {
        return subject_id;
    }


    public String getSubject_name() {
        return subject_name;
    }

    public String getImportance() {
        return importance;
    }

    public int getEcts_points() {
        return ects_points;
    }

    public boolean isIs_lecture() {
        return is_lecture;
    }

    public boolean isIs_exercise() {
        return is_exercise;
    }

    public boolean isIs_lab() {
        return is_lab;
    }

    public boolean isPassed() {
        return passed;
    }

    public int getProfile_id() {
        return profile_id;
    }


}
