package com.example.passit.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.passit.db.entities.Subject;

import java.util.List;

public class SubjectWithTasks {
    @Embedded
    public Subject subject;
    @Relation(
            parentColumn = "subject_id",
            entityColumn = "subject_id"
    )
    public List<Task> tasks;
}
