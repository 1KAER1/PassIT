package com.example.passit;

import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayChannelID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationText;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationTitle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.notificationbrodcasts.ReminderBroadcast;
import com.example.passit.rvadapters.ResponsibilitiesMainRVAdapter;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageButton addSubjectButton, respButton, notesButton, calendarButton;
    private RecyclerView importantRespRV, overdueRespRV;
    private ResponsibilitiesMainRVAdapter adapter, adapter2;
    private MaterialAutoCompleteTextView noOfSubjectsTV, passedTV, taskNo, testNo;
    private TextView profileNameTV, userNameTV;
    private long pressedTime;
    private String notificationType = "Daily";
    private AppDatabase db;
    private List<Profile> profilesList = new ArrayList<>();
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<String> responsibilitiesDates = new ArrayList<>();
    private List<Responsibility> responsibilitiesRV = new ArrayList<>();
    private List<Responsibility> earliestDate = new ArrayList<>();
    private List<Responsibility> overdueResponsibilities = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    final DateFormat format = new SimpleDateFormat("d/MM/yyyy");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        addSubjectButton = findViewById(R.id.subjectsBtn);
        respButton = findViewById(R.id.respButton);
        notesButton = findViewById(R.id.notesViewBtn);
        calendarButton = findViewById(R.id.calendarViewBtn);
        importantRespRV = findViewById(R.id.importantRespRV);
        overdueRespRV = findViewById(R.id.overdueRespRV);
        profileNameTV = findViewById(R.id.profileNameTV);
        userNameTV = findViewById(R.id.userNameTV);
        noOfSubjectsTV = findViewById(R.id.noOfSubjectsTV);
        passedTV = findViewById(R.id.passedTV);
        taskNo = findViewById(R.id.taskNo);
        testNo = findViewById(R.id.testNo);
        createNotificationChannel();

        db = AppDatabase.getDbInstance(this);

        profilesList = db.profileDao().getAllProfiles();

        if (profilesList.isEmpty()) {
            addNewUser();
        }

        if (db.profileDao().getDailyNotification(notificationType) < 1) {
            createDailyNotification();
        }

        userNameTV.setText("Cześć " + db.profileDao().getUserName() + "!");
        profileNameTV.setText(db.profileDao().getActiveProfileName() + " semestr " + db.profileDao().getActiveProfileSemester());

        responsibilitiesList = db.profileDao().getAllResponsibilities();

        if (responsibilitiesList != null) {
            checkResponsibilitiesDelay();
        }

        responsibilitiesDates = db.profileDao().getUndelayedResponsibilitiesDates();
        if (!responsibilitiesDates.isEmpty()) {
            sortRespDates();
            earliestDate = db.profileDao().getRespWithDate(responsibilitiesDates.get(0));

            adapter = new ResponsibilitiesMainRVAdapter(earliestDate);
            importantRespRV.setAdapter(adapter);
        }

        overdueResponsibilities = db.profileDao().getOverdueResponsibilities();

        if (!overdueResponsibilities.isEmpty()) {
            adapter2 = new ResponsibilitiesMainRVAdapter(overdueResponsibilities);
            overdueRespRV.setAdapter(adapter2);
        }

        noOfSubjectsTV.setText(String.valueOf(db.profileDao().getSubjectCount()));
        passedTV.setText(String.valueOf(db.profileDao().getPassedSubjectCount()));
        taskNo.setText(String.valueOf(db.profileDao().getTaskCount()));
        testNo.setText(String.valueOf(db.profileDao().getTestCount()));


        addSubjectButton.setOnClickListener(view -> openView(SubjectsView.class));
        respButton.setOnClickListener(view -> openView(ResponsibilitiesView.class));
        notesButton.setOnClickListener(view -> openView(NotesView.class));
        calendarButton.setOnClickListener(view -> openView(CalendarActivity.class));

    }

    public void sortRespDates() {
        responsibilitiesDates.sort((o1, o2) -> {
            try {
                return Objects.requireNonNull(format.parse(o1)).compareTo(format.parse(o2));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    public void checkResponsibilitiesDelay() {
        try {
            for (Responsibility resp : responsibilitiesList) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
                Date date1 = sdf.parse(resp.getDate_due());
                Date date2 = sdf.parse(getTodaysDate());

                assert date1 != null;
                if (date1.before(date2)) {
                    db.profileDao().markDelayedResp(resp.getResp_id());
                } else if (date1.equals(date2)) {
                    LocalTime hourDue = LocalTime.parse(resp.getHour_due());
                    LocalTime currentTime = LocalTime.parse(getCurrentTime());
                    if (currentTime.isAfter(hourDue)) {
                        db.profileDao().markDelayedResp(resp.getResp_id());
                    }
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void createDailyNotification() {
        Intent intent = new Intent(this, ReminderBroadcast.class);

        String title, message;
        title = "Coś do dodania?";
        message = "Dodaj nowe zadania lub notatki, żeby o nich nie zapomnieć!";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        addNotificationToDatabase(title, message, calendar.getTimeInMillis(), notificationType);

        int notificationId = db.profileDao().getDailyNotification(notificationType);
        intent.putExtra(String.valueOf(notificationId), notificationId);
        intent.putExtra(notificationTitle, title);
        intent.putExtra(notificationText, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                delayNotificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    public void addNotificationToDatabase(String title, String message, long time, String notificationType) {
        Notification notification = new Notification();
        notification.notification_type = notificationType;
        notification.notification_title = title;
        notification.notification_text = message;
        notification.time_to_trigger = time;
        System.out.println("Last added ID: " + db.profileDao().getLastRespId());
        db.profileDao().insertNotification(notification);
    }

    private void createNotificationChannel() {
        CharSequence name = "Delayed Responsibilities";
        String desc = "Used for reminding about delayed responsibilities";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(delayChannelID, name, importance);
        channel.setDescription(desc);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private String getCurrentTime() {

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return String.valueOf(time.format(formatter));
    }

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    public void addNewUser() {
        Intent intent = new Intent(this, AddNewUser.class);
        startActivity(intent);
    }

    public void openView(Class<?> viewClass) {
        Intent intent = new Intent(this, viewClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            this.finishAffinity();
        } else {
            Toast.makeText(this, "Kliknij jeszcze raz, aby zamknąć", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}