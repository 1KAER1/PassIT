package com.example.passit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.LessonDate;
import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Subject;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM subject")
    List<Subject> getAllSubjects();

    //Subject
    @Insert
    void insertProfile(Profile... profiles);
    @Delete
    void deleteProfile(Profile... profile);

    //Subject
    @Insert
    void insertSubject(Subject... subjects);
    @Delete
    void deleteSubject(Subject... subject);

    //Lesson
    @Insert
    void insertLesson(Lesson... lessons);
    @Delete
    void deleteLesson(Lesson... lesson);

    //LessonDate
    @Insert
    void insertLessonDate(LessonDate... lessonDates);
    @Delete
    void deleteLessonDate(LessonDate... lessonDate);
}
