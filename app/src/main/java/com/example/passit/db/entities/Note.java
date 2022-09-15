package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int note_id;
    public String note_title;
    @Nullable
    public String note_description;
    public int profile_id;

    public int getNote_id() {
        return note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    @Nullable
    public String getNote_description() {
        return note_description;
    }

    public int getProfile_id() {
        return profile_id;
    }
}
