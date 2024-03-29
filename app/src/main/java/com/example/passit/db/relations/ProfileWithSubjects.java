package com.example.passit.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.passit.db.entities.Note;
import com.example.passit.db.entities.Profile;

import java.util.List;

public class ProfileWithSubjects {
    @Embedded
    public Profile profile;
    @Relation(
            parentColumn = "profile_id",
            entityColumn = "profile_id"
    )
    public List<Note> notes;
}
