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

    @Query("SELECT is_lecture FROM Subject WHERE subject_name = :subjectName")
    boolean getSubjectLecture(String subjectName);

    @Query("SELECT is_exercise FROM Subject WHERE subject_name = :subjectName")
    boolean getSubjectExercise(String subjectName);

    @Query("SELECT is_lab FROM Subject WHERE subject_name = :subjectName")
    boolean getSubjectLab(String subjectName);

    //Task
    @Insert
    void insertTask(Task... tasks);

    @Query("UPDATE Task SET task_name=:taskName, importance=:importance, date_due=:dateDue, hour_due=:hourDue," +
            " description=:description, subject_type=:subjectType, subject_id=:subjectId WHERE task_id=:taskId")
    void updateTask(String taskName, String importance, String dateDue, String hourDue, String description, String subjectType,
                    int subjectId, int taskId);

    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    @Query("SELECT * FROM Task WHERE task_id = :taskId")
    List<Task> getTaskWithId(int taskId);

    @Query("DELETE FROM Task WHERE task_id = :taskId")
    void deleteTask(int taskId);

    @Query("UPDATE Task SET finished = 1 WHERE task_id = :taskId")
    void setFinishedTask(int taskId);

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
