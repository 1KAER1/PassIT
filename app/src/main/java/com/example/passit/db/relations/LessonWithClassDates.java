package com.example.passit.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

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
