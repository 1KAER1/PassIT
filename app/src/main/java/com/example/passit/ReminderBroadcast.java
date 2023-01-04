package com.example.passit;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    public static int notificationID = 1;
    public static String channelID = "channel1";
    public static String notificationTitle = "notificationTitle";
    public static String notificationText = "notificationText";

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_baseline_notes_24)
                .setContentTitle(intent.getStringExtra(notificationTitle))
                .setContentText(intent.getStringExtra(notificationText))
                .build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder);
    }
}
