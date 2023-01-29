package com.example.passit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.passit.db.entities.Note;
import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.User;

import java.util.List;

@Dao
public interface ProfileDao {


    //USER
    @Insert
    void insertUser(User... users);

    @Update
    void updateUser(User... users);

    @Query("SELECT user_name FROM User")
    String getUserName();

    @Query("SELECT * FROM User ORDER BY user_id DESC LIMIT 1")
    int getLastUserId();
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

    @Query("DELETE FROM Note WHERE note_id = :noteId")
    void deleteNoteWithId(int noteId);

    @Query("SELECT * FROM Note WHERE note_id = :noteId")
    List<Note> getNoteWithId(int noteId);

    @Query("UPDATE Note SET note_title=:noteTitle, note_description=:noteDescription  WHERE note_id=:noteId")
    void updateNote(String noteTitle, String noteDescription, int noteId);

    //Subject
    @Insert
    void insertSubject(Subject... subjects);

    @Query("SELECT COUNT(subject_id) FROM Subject")
    int getSubjectCount();

    @Query("SELECT * FROM subject")
    List<Subject> getAllSubjects();

    @Query("SELECT COUNT(subject_id) FROM Subject WHERE passed = 1")
    int getPassedSubjectCount();

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

    //NOTIFICATION
    @Insert
    void insertNotification(Notification... notifications);

    @Query("SELECT notification_id FROM Notification WHERE resp_id = :respId AND notification_type = :notificationType")
    int getNotificationId(int respId, String notificationType);

    @Query("SELECT * FROM Notification WHERE notification_id = :notificationId")
    List<Notification> getNotificationById(int notificationId);

    @Query("SELECT notification_id FROM Notification WHERE notification_type = :notificationType")
    int getDailyNotification(String notificationType);

    @Query("DELETE FROM Notification WHERE notification_id = :notificationId")
    void deleteNotificationById(int notificationId);

    //RESPONSIBILITY
    @Insert
    void insertResponsibility(Responsibility... responsibilities);

    @Query("SELECT * FROM Responsibility ORDER BY resp_id DESC LIMIT 1")
    int getLastRespId();

    @Query("SELECT * FROM Responsibility")
    List<Responsibility> getAllResponsibilities();

    @Query("SELECT * FROM Responsibility WHERE finished = 0")
    List<Responsibility> getAllUnfinishedResponsibilities();

    @Query("SELECT * FROM Responsibility WHERE importance = 'high' AND finished = 0")
    List<Responsibility> getHighImportanceResponsibilities();

    @Query("SELECT * FROM Responsibility WHERE importance = 'medium' AND finished = 0")
    List<Responsibility> getMediumImportanceResponsibilities();

    @Query("SELECT * FROM Responsibility WHERE importance = 'normal' AND finished = 0")
    List<Responsibility> getNormalImportanceResponsibilities();

    @Query("SELECT COUNT(resp_id) FROM Responsibility WHERE responsibility_type = 'Task'")
    int getTaskCount();

    @Query("SELECT COUNT(resp_id) FROM Responsibility WHERE responsibility_type = 'Test'")
    int getTestCount();

    @Query("UPDATE Responsibility SET resp_name=:respName, responsibility_type=:respType,importance=:importance, date_due=:dateDue, hour_due=:hourDue," +
            " description=:description, subject_type=:subjectType, fileUri=:fileUri ,subject_id=:subjectId WHERE resp_id=:respId")
    void updateResponsibility(String respName, String respType, String importance, String dateDue, String hourDue, String description, String subjectType, String fileUri,
                              int subjectId, int respId);

    @Query("DELETE FROM Responsibility WHERE resp_id = :respId")
    void deleteResponsibility(int respId);

    @Query("DELETE FROM Responsibility WHERE subject_id = :subjectId")
    void deleteResponsibilityWithSubject(int subjectId);

    @Query("UPDATE Responsibility SET finished = 1 WHERE resp_id = :respId")
    void setFinishedResponsibility(int respId);

    @Query("UPDATE Responsibility SET finished = 0 WHERE resp_id = :respId")
    void setUnfinishedResponsibility(int respId);

    @Query("UPDATE Responsibility SET delayed = 1 WHERE resp_id = :respId")
    void markDelayedResp(int respId);

    @Query("UPDATE Responsibility SET delayed = 0 WHERE resp_id = :respId")
    void markUndelayedResp(int respId);

    @Query("SELECT finished FROM Responsibility WHERE resp_id = :respId")
    boolean getResponsibilityState(int respId);

    @Query("SELECT * FROM Responsibility WHERE resp_id = :respId")
    List<Responsibility> getResponsibilityWithId(int respId);

    @Query("SELECT * FROM Responsibility WHERE subject_id = :subjectId")
    List<Responsibility> getResponsibilityWithSubjectId(int subjectId);

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
}
