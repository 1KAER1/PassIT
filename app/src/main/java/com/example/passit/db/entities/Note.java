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
    public int test_id;
    public String note_title;
    public String note_description;
    @Nullable
    @ColumnInfo(defaultValue = "null")
    public String subject_type;
    @Nullable
    @ColumnInfo(defaultValue = "null")
    public int subject_id;
}
