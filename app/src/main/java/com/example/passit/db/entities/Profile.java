package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {
                "profile_name"
        }, unique = true)
})
public class Profile {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int profile_id;
    public String profile_name;
    public int semester;
    public boolean isActive;
    public int user_id;

    public int getProfile_id() {
        return profile_id;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public int getSemester() {
        return semester;
    }

    public boolean isActive() {
        return isActive;
    }
}
