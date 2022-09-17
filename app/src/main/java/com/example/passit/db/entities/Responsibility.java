package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Responsibility {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int resp_id;
    public String resp_name;
    public String responsibility_type;
    public String importance;
    public String date_due;
    public String hour_due;
    @Nullable
    public String description;
    public String subject_type;
    @ColumnInfo(defaultValue = "false")
    public boolean finished;
    public int subject_id;

    public int getResp_id() {
        return resp_id;
    }

    public String getResp_name() {
        return resp_name;
    }

    public String getResponsibility_type() {
        return responsibility_type;
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

    @Nullable
    public String getDescription() {
        return description;
    }

    public String getSubject_type() {
        return subject_type;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getSubject_id() {
        return subject_id;
    }
}
