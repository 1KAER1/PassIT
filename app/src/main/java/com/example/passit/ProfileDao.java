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

    @Query("SELECT * FROM subject WHERE subject_id = :subjectID")
    List<Subject> getSubjectWithId(int subjectID);

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

    @Query("SELECT * FROM Task WHERE task_id = :taskId")
    List<Task> getTaskWithId(int taskId);

    //Test
    @Insert
    void insertTest(Test... tests);

    @Delete
    void deleteTest(Test... test);

    @Query("SELECT * FROM Test")
    List<Test> getAllTests();

    @Query("SELECT * FROM Test WHERE test_id = :testId")
    List<Test> getTestWithId(int testId);

    @Query("SELECT * FROM Test WHERE importance = :importance")
    List<Test> getTestsWithImportance(String importance);

    @Query("SELECT * FROM Test WHERE test_name LIKE :description")
    String getSearchResults(String description);


    //Lesson
    @Insert
    void insertLesson(Lesson... lessons);

    @Delete
    void deleteLesson(Lesson... lesson);

    @Query("SELECT * FROM Lesson WHERE subject_id = :subjectId AND lesson_type = :lessonType")
    List<Lesson> getLessonWithId(int subjectId, String lessonType);

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

    //Lesson Date
    @Query("SELECT * FROM LessonDate WHERE lesson_id = :lessonId")
    List<LessonDate> getLessonDate(int lessonId);

    //Profile
    @Query("SELECT * FROM Profile")
    List<Profile> getAllProfiles();

    @Query("SELECT profile_id FROM profile WHERE isActive = 1")
    int getActiveProfile();

    @Query("SELECT profile_name FROM profile WHERE isActive = 1")
    String getActiveProfileName();

    @Query("UPDATE profile SET isActive = 0 WHERE isActive = 1")
    void deactivateProfiles();

    @Query("UPDATE profile SET isActive = 1 WHERE profile_name = :profileName")
    void activateProfile(String profileName);

    @Transaction
    @Query("SELECT profile_name FROM Profile")
    List<String> getAllProfilesNames();


}
