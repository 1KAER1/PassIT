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
    public String hour_due;
    public String description;
    @ColumnInfo(defaultValue = "false")
    public boolean passed;
    public int subject_id;

    public int getTest_id() {
        return test_id;
    }

    public String getTest_name() {
        return test_name;
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

    public boolean isPassed() {
        return passed;
    }

    public int getSubject_id() {
        return subject_id;
    }
}
