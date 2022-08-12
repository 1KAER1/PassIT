package com.example.passit.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Subject;

import java.util.List;

public class ProfileWithSubjects {
    @Embedded
    public Profile profile;
    @Relation(
            parentColumn = "profile_id",
            entityColumn = "profile_id"
    )
    public List<Subject> subjects;
}
