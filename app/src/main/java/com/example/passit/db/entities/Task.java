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
    public String hour_due;
    public String description;
    @ColumnInfo(defaultValue = "false")
    public boolean finished;
    public int subject_id;

    public int getTask_id() {
        return task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getImportance() {
        return importance;
    }

    public String getDate_due() {
        return date_due;
    }

    public String getHour_due() {
        return hour_due;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getSubject_id() {
        return subject_id;
    }

}
