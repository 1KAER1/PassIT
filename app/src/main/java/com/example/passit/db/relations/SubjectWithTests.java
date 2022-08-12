package com.example.passit.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.passit.db.entities.Test;

import java.util.List;

public class SubjectWithTests {
    @Embedded
    @Relation(
            parentColumn = "subject_id",
            entityColumn = "subject_id"
    )
    public List<Test> tests;
}