package com.example.passit.notificationbrodcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.passit.R;

import java.util.Date;

public class ReminderBroadcast extends BroadcastReceiver {


    public static int delayedRespID = 3;
    public static int summary_id = 0;

    //1 HOUR BEFORE DEADLINE NOTIFICATION
    public static int notificationID = 1;
    public static String channelID = "channel1";
    public static String notificationTitle = "notificationTitle";
    public static String notificationText = "notificationText";

    public static int delayNotificationID = 2;
    public static String delayChannelID = "channel2";
    public static String delayNotificationTitle = "notificationTitle";
    public static String delayNotificationText = "notificationText";

    public static int delaysNotificationID = 3;
    public static String delaysChannelID = "channel3";
    public static String delaysNotificationTitle = "notificationTitle";
    public static String delaysNotificationText = "notificationText";


    @Override
    public void onReceive(Context context, Intent intent) {

        int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Notification builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_baseline_notes_24)
                .setContentTitle(intent.getStringExtra(notificationTitle))
                .setContentText(intent.getStringExtra(notificationText))
                .build();

        Notification delayNotification = new NotificationCompat.Builder(context, delayChannelID)
                .setSmallIcon(R.drawable.ic_baseline_notes_24)
                .setContentTitle(intent.getStringExtra(delayNotificationTitle))
                .setContentText(intent.getStringExtra(delayNotificationText))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getIntExtra(String.valueOf(notificationID), notificationID), builder);

    }
}
