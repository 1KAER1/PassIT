package com.example.passit;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.passit.db.entities.LessonDate;
import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.Note;
import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.db.entities.Test;

@Database(
        entities = {
                Lesson.class,
                LessonDate.class,
                Profile.class,
                Subject.class,
                Task.class,
                Test.class,
                Note.class
        }, version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProfileDao profileDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "passit_db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
