package com.example.passit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.LessonDate;
import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.db.entities.Test;
import com.example.passit.db.relations.SubjectWithLessons;

import java.util.ArrayList;
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

    @Query("SELECT subject_id FROM subject WHERE subject_name = :subjectName")
    int getSubjectId(String subjectName);

    @Query("SELECT subject_name FROM subject WHERE subject_id = :subjectID")
    String getSubjectName(int subjectID);

    @Transaction
    @Query("SELECT subject_name FROM Subject")
    List<String> getAllSubjectsNames();

    //Task
    @Insert
    void insertTask(Task... tasks);

    @Delete
    void deleteTask(Task... task);

    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    //Test
    @Insert
    void insertTest(Test... tests);

    @Delete
    void deleteTest(Test... test);

    @Query("SELECT * FROM Test")
    List<Test> getAllTests();

    //Lesson
    @Insert
    void insertLesson(Lesson... lessons);

    @Delete
    void deleteLesson(Lesson... lesson);

    @Query("SELECT lesson_id FROM Lesson WHERE lesson_type = :lessonType AND subject_id = :subjectId")
    int getLessonId(String lessonType, int subjectId);

    //LessonDate
    @Insert
    void insertLessonDate(LessonDate... lessonDates);

    @Delete
    void deleteLessonDate(LessonDate... lessonDate);

    @Transaction
    @Query("SELECT * FROM Subject")
    List<SubjectWithLessons> getSubjectWithLessons();


    //Profile
    @Query("SELECT profile_id FROM profile WHERE isActive = 1")
    int getActiveProfile();

    @Query("SELECT profile_name FROM profile WHERE isActive = 1")
    String getActiveProfileName();

    @Query("UPDATE profile SET isActive = 0 WHERE isActive = 1")
    void deactivateProfiles();

    @Query("UPDATE profile SET isActive = 1 WHERE profile_name = :profileName")
    void activateProfile(String profileName);


}
