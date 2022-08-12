package com.example.passit.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.LessonDate;

import java.util.List;

public class LessonWithClassDates {
    @Embedded
    public Lesson lesson;
    @Relation(
            parentColumn = "lesson_id",
            entityColumn = "lesson_id"
    )
    public List<LessonDate> lessonDates;
}
