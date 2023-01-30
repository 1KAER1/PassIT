package com.example.passit;

import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationText;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationTitle;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationText;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationTitle;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.notificationbrodcasts.ReminderBroadcast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationSender {

    private final Context context;
    private final AddResponsibility addResponsibility = new AddResponsibility();
    private String title, message;

    public NotificationSender(Context context) {
        this.context = context;
    }

    public void sendNotification(long time, int responsibilityId, String notificationType) {
        Intent intent = new Intent(context, ReminderBroadcast.class);

        AppDatabase db = AppDatabase.getDbInstance(context);
        List<Responsibility> respList = db.profileDao().getResponsibilityWithId(responsibilityId);


        switch (notificationType) {
            case "Delay":
                title = "Minął termin!";
                message = "Minął termin na oddanie: \"" + respList.get(0).getResp_name() + "\":\nTermin na oddanie: " + respList.get(0).getDate_due() + ", " + respList.get(0).getHour_due();
                break;
            case "Reminder":
                title = "Zbliża się termin oddania";
                message = "Ostateczny termin na oddanie \"" + respList.get(0).getResp_name() + "\":\n" + respList.get(0).getDate_due() + ", " + respList.get(0).getHour_due();
                break;
        }

        addNotificationToDatabase(title, message, time, notificationType, responsibilityId, db);

        int notificationId = db.profileDao().getNotificationId(responsibilityId, notificationType);
        intent.putExtra(String.valueOf(notificationId), notificationId);
        intent.putExtra(notificationTitle, title);
        intent.putExtra(notificationText, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
    }

    public void cancelNotification(int notificationId) {
        Intent intent = new Intent(context, ReminderBroadcast.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        notificationManager.cancel(notificationId);
    }

    public void addNotificationToDatabase(String title, String message, long time, String notificationType, int notificationRespId, AppDatabase db) {
        Notification notification = new Notification();
        notification.notification_type = notificationType;
        notification.notification_title = title;
        notification.notification_text = message;
        notification.time_to_trigger = time;
        notification.resp_id = notificationRespId;
        Log.d("MyTag77", "Title: " + title + " message: " + message + " TIME: " + time + "NOTIFICATION TYPE: " + notificationType + " RESP ID: " + notificationRespId);
        db.profileDao().insertNotification(notification);
    }
}
