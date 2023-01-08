package com.example.passit.db.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notification {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int notification_id;
    public String notification_type;
    public String notification_title;
    public String notification_text;
    public long time_to_trigger;
    public int resp_id;

    public int getNotification_id() {
        return notification_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public long getTime_to_trigger() {
        return time_to_trigger;
    }

    public int getResp_id() {
        return resp_id;
    }
}
