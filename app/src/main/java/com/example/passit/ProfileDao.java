package com.example.passit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.LessonDate;
import com.example.passit.db.entities.Note;
import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.db.entities.Test;
import com.example.passit.db.relations.SubjectWithLessons;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM subject")
    List<Subject> getAllSubjects();


    //Profile
    @Insert
    void insertProfile(Profile... profiles);

    @Delete
    void deleteProfile(Profile... profile);

    @Query("SELECT * FROM Profile")
    List<Profile> getAllProfiles();

    @Query("SELECT profile_id FROM profile WHERE isActive = 1")
    int getActiveProfile();

    @Query("SELECT profile_name FROM profile WHERE isActive = 1")
    String getActiveProfileName();

    @Query("SELECT semester FROM profile WHERE isActive = 1")
    int getActiveProfileSemester();

    @Query("UPDATE profile SET isActive = 0 WHERE isActive = 1")
    void deactivateProfiles();

    @Query("UPDATE profile SET isActive = 1 WHERE profile_name = :profileName")
    void activateProfile(String profileName);

    @Transaction
    @Query("SELECT profile_name FROM Profile")
    List<String> getAllProfilesNames();

    //Note
    @Query("SELECT * FROM Note")
    List<Note> getAllNotes();

    @Insert
    void insertNote(Note... notes);

    @Query("SELECT * FROM Note WHERE note_id = :noteId")
    List<Note> getNoteWithId(int noteId);

    @Query("UPDATE Note SET note_title=:noteTitle, note_description=:noteDescription  WHERE note_id=:noteId")
    void updateNote(String noteTitle, String noteDescription, int noteId);


    //Subject
    @Insert
    void insertSubject(Subject... subjects);

    @Query("DELETE FROM Subject WHERE subject_id = :subjectId")
    void deleteSubject(int subjectId);

    @Query("SELECT subject_id FROM subject WHERE subject_name = :subjectName")
    int getSubjectId(String subjectName);

    @Query("SELECT subject_name FROM subject WHERE subject_id = :subjectID")
    String getSubjectName(int subjectID);

    @Query("SELECT * FROM subject WHERE subject_id = :subjectID")
    List<Subject> getSubjectWithId(int subjectID);

    @Query("SELECT passed FROM Subject WHERE subject_id = :subjectId")
    boolean getSubjectState(int subjectId);

    @Transaction
    @Query("SELECT subject_name FROM Subject")
    List<String> getAllSubjectsNames();

    @Query("SELECT is_lecture FROM Subject WHERE subject_name = :subjectName")
    boolean getSubjectLecture(String subjectName);

    @Query("SELECT is_exercise FROM Subject WHERE subject_name = :subjectName")
    boolean getSubjectExercise(String subjectName);

    @Query("SELECT is_lab FROM Subject WHERE subject_name = :subjectName")
    boolean getSubjectLab(String subjectName);

    @Query("UPDATE Subject SET passed = 1 WHERE subject_id = :subjectId")
    void setPassedSubject(int subjectId);

    @Query("UPDATE Subject SET passed = 0 WHERE subject_id = :subjectId")
    void setSubjectInProgress(int subjectId);

    @Query("UPDATE Subject SET subject_name=:subjectName, is_lecture=:isLecture, is_exercise=:isExercise," +
            "is_lab=:isLab, importance=:importance, " +
            "ects_points=:ectsPoints WHERE subject_id=:subjectId")
    void updateSubject(String subjectName, String importance, String ectsPoints, boolean isLecture, boolean isExercise,
                       boolean isLab, int subjectId);

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

    @Query("UPDATE Task SET finished = 0 WHERE task_id = :taskId")
    void setUnfinishedTask(int taskId);

    @Query("SELECT finished FROM Task WHERE task_id = :taskId")
    boolean getTaskState(int taskId);

    //Test
    @Insert
    void insertTest(Test... tests);

    @Delete
    void deleteTest(Test... test);

    @Query("SELECT * FROM Test")
    List<Test> getAllTests();

    @Query("UPDATE Test SET test_name=:testName, importance=:importance, date_due=:dateDue, hour_due=:hourDue," +
            " description=:description, subject_type=:subjectType, subject_id=:subjectId WHERE test_id=:testId")
    void updateTest(String testName, String importance, String dateDue, String hourDue, String description, String subjectType,
                    int subjectId, int testId);

    @Query("DELETE FROM Test WHERE test_id = :testId")
    void deleteTest(int testId);

    @Query("UPDATE Test SET passed = 1 WHERE test_id = :testId")
    void setPassedTest(int testId);

    @Query("UPDATE Test SET passed = 0 WHERE test_id = :testId")
    void setUnfinishedTest(int testId);

    @Query("SELECT passed FROM Test WHERE test_id = :testId")
    boolean getTestState(int testId);

    @Query("SELECT * FROM Test WHERE test_id = :testId")
    List<Test> getTestWithId(int testId);

    @Query("SELECT * FROM Test WHERE importance = :importance")
    List<Test> getTestsWithImportance(String importance);

    //RESPONSIBILITY
    @Insert
    void insertResponsibility(Responsibility... responsibilities);

    @Query("SELECT * FROM Responsibility")
    List<Responsibility> getAllResponsibilities();

    @Query("UPDATE Responsibility SET resp_name=:respName, responsibility_type=:respType,importance=:importance, date_due=:dateDue, hour_due=:hourDue," +
            " description=:description, subject_type=:subjectType, subject_id=:subjectId WHERE resp_id=:respId")
    void updateResponsibility(String respName, String respType, String importance, String dateDue, String hourDue, String description, String subjectType,
                              int subjectId, int respId);

    @Query("DELETE FROM Responsibility WHERE resp_id = :respId")
    void deleteResponsibility(int respId);

    @Query("UPDATE Responsibility SET finished = 1 WHERE resp_id = :respId")
    void setFinishedResponsibility(int respId);

    @Query("UPDATE Responsibility SET finished = 0 WHERE resp_id = :respId")
    void setUnfinishedResponsibility(int respId);

    @Query("UPDATE Responsibility SET delayed = 1 WHERE resp_id = :respId")
    void markDelayedResp(int respId);

    @Query("SELECT finished FROM Responsibility WHERE resp_id = :respId")
    boolean getResponsibilityState(int respId);

    @Query("SELECT * FROM Responsibility WHERE resp_id = :respId")
    List<Responsibility> getResponsibilityWithId(int respId);

    @Query("SELECT * FROM Responsibility WHERE date_due = :dateDue")
    List<Responsibility> getResponsibilityWithDate(String dateDue);

    @Query("SELECT date_due FROM Responsibility")
    List<String> getResponsibilitiesDates();

    @Query("SELECT date_due FROM Responsibility WHERE delayed = 0 AND finished = 0")
    List<String> getUndelayedResponsibilitiesDates();

    @Query("SELECT * FROM Responsibility WHERE delayed = 1 AND finished = 0")
    List<Responsibility> getOverdueResponsibilities();

    @Query("SELECT * FROM Responsibility WHERE date_due=:date AND finished = 0")
    List<Responsibility> getRespWithDate(String date);

    @Query("SELECT * FROM Responsibility WHERE importance = :importance")
    List<Responsibility> getResponsibilitiesWithImportance(String importance);


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




}
