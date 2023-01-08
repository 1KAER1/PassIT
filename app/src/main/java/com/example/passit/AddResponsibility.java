package com.example.passit;

import static com.example.passit.notificationbrodcasts.ReminderBroadcast.channelID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayChannelID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationText;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayNotificationTitle;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationText;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.notificationTitle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.notificationbrodcasts.ReminderBroadcast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddResponsibility extends AppCompatActivity {

    public static String NOTIFICATION_CHANNEL_ID = "777";
    public static String default_notification_id = "default";
    private TextInputLayout respName, description;
    private TextInputEditText respNameET, descET;
    private TextView headlineTV;
    private Spinner subjectSpinner, subjectTypeSpinner;
    private Button datePickerButton, nextButton, timeButton;
    private RadioButton normalImportance, mediumImportance, highImportance, taskRB, testRB;
    private RadioGroup importanceRadioGroup;
    private int hour, minute;
    private List<String> subjectsList = new ArrayList<>();
    private List<String> subjectTypeList = new ArrayList<>();
    private List<Responsibility> respList = new ArrayList<>();
    private AppDatabase db;
    private DatePickerDialog datePickerDialog;
    private String selectedImportance = null;
    private long pressedTime;
    private int respId;
    private boolean isEdit, fromCalendar, fromMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_responsibility);

        db = AppDatabase.getDbInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEdit = true;
            respId = extras.getInt("respId");
            if (extras.getString("calendar") != null) {
                fromCalendar = true;
            }
            if (extras.getString("menu") != null) {
                fromMenu = true;
            }
            respList = db.profileDao().getResponsibilityWithId(respId);
        }

        initDatePicker();
        createNotificationChannels();

        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        importanceRadioGroup = findViewById(R.id.importanceRadioGroup);
        respName = findViewById(R.id.noteTitle);
        description = findViewById(R.id.noteDescription);
        respNameET = findViewById(R.id.respName);
        descET = findViewById(R.id.respDesc);
        subjectSpinner = findViewById(R.id.assignedSubjectTV);
        subjectTypeSpinner = findViewById(R.id.subjectTypeSpinner);
        datePickerButton = findViewById(R.id.datePicker);
        timeButton = findViewById(R.id.timePicker);
        nextButton = findViewById(R.id.nextBtn);
        headlineTV = findViewById(R.id.headlineTV);
        taskRB = findViewById(R.id.taskRB);
        testRB = findViewById(R.id.testRB);

        datePickerButton.setText(getTodaysDate());
        timeButton.setText(getCurrentTime());


        subjectsList = db.profileDao().getAllSubjectsNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_layout, subjectsList);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
        subjectSpinner.setAdapter(adapter);

        if (isEdit) {
            nextButton.setText("ZAKTUALIZUJ");
            int selectionPosition = adapter.getPosition(db.profileDao().getSubjectName(respList.get(0).getSubject_id()));
            subjectSpinner.setSelection(selectionPosition);
            respNameET.setText(respList.get(0).getResp_name());
            datePickerButton.setText(respList.get(0).getDate_due());
            timeButton.setText(respList.get(0).getHour_due());
            descET.setText(respList.get(0).getDescription());

            switch (respList.get(0).getImportance()) {
                case "normal":
                    normalImportance.setChecked(true);
                    break;
                case "medium":
                    mediumImportance.setChecked(true);
                    break;
                case "high":
                    highImportance.setChecked(true);
                    break;
            }

            switch (respList.get(0).getResponsibility_type()) {
                case "Task":
                    taskRB.setChecked(true);
                    break;
                case "Test":
                    testRB.setChecked(true);
                    break;
            }
        }


        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newItem = subjectSpinner.getSelectedItem().toString();

                subjectTypeList.clear();

                if (db.profileDao().getSubjectLecture(newItem)) {
                    subjectTypeList.add("WYKŁAD");
                }

                if (db.profileDao().getSubjectExercise(newItem)) {
                    subjectTypeList.add("ĆWICZENIA");
                }

                if (db.profileDao().getSubjectLab(newItem)) {
                    subjectTypeList.add("LABORATORIA");
                }


                ArrayAdapter<String> subjectTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_layout, subjectTypeList);
                subjectTypeAdapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
                subjectTypeSpinner.setAdapter(subjectTypeAdapter);

                if (isEdit) {
                    int selectionPosition2 = subjectTypeAdapter.getPosition(respList.get(0).getSubject_type());
                    subjectTypeSpinner.setSelection(selectionPosition2);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        timeButton.setOnClickListener(view -> popTimePicker());
        datePickerButton.setOnClickListener(this::openDatePicker);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    updateResponsibility();
                } else {
                    addResponsibilityToDatabase();
                }
            }
        });
    }

    public void addResponsibilityToDatabase() {
        if (checkInput() && checkDate()) {
            addDatabaseEntry();
            createDelayNotification(db.profileDao().getLastRespId());
            createNotification(db.profileDao().getLastRespId());
            returnToView();
        } else {
            Toast.makeText(this, "Niepoprawne dane!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void createNotification(int responsibilityId) {
        Intent intent = new Intent(this, ReminderBroadcast.class);

        long time = getNotificationTime();
        String title, message, notificationType;
        notificationType = "Reminder";
        Log.d("myTag2", "Time time time: " + time);

        if (time == 2) {
            title = "Minął termin!";
            message = "Minął termin na oddanie: \"" + respNameET.getText().toString() + "\":\nTermin na oddanie: " + datePickerButton.getText().toString() + ", " + timeButton.getText().toString();
        } else {
            title = "Zbliża się termin oddania";
            message = "Ostateczny termin na oddanie \"" + respNameET.getText().toString() + "\":\n" + datePickerButton.getText().toString() + ", " + timeButton.getText().toString();
        }

        addNotificationToDatabase(title, message, time, notificationType);
        int notificationId = db.profileDao().getNotificationId(responsibilityId, notificationType);

        intent.putExtra(String.valueOf(notificationID), notificationId);
        intent.putExtra(notificationTitle, title);
        intent.putExtra(notificationText, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
    }

    public void createDelayNotification(int responsibilityId) {
        Intent intent = new Intent(this, ReminderBroadcast.class);

        String title, message, notificationType;
        notificationType = "Delay";

        String[] dateParts = datePickerButton.getText().toString().split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        String[] time = timeButton.getText().toString().split(":");
        int hour = Integer.parseInt(time[0].trim());
        int minute = Integer.parseInt(time[1].trim());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        title = "Minął termin!";
        message = "Minął termin na oddanie: \"" + respNameET.getText().toString() + "\":\nTermin na oddanie: " + datePickerButton.getText().toString() + ", " + timeButton.getText().toString();

        addNotificationToDatabase(title, message, calendar.getTimeInMillis(), notificationType);

        int notificationId = db.profileDao().getNotificationId(responsibilityId, notificationType);
        intent.putExtra(String.valueOf(notificationId), notificationId);
        intent.putExtra(delayNotificationTitle, title);
        intent.putExtra(delayNotificationText, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                delayNotificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }

    public void addNotificationToDatabase(String title, String message, long time, String notificationType) {
        Notification notification = new Notification();
        notification.notification_type = notificationType;
        notification.notification_title = title;
        notification.notification_text = message;
        notification.time_to_trigger = time;
        notification.resp_id = db.profileDao().getLastRespId();
        System.out.println("Last added ID: " + db.profileDao().getLastRespId());
        db.profileDao().insertNotification(notification);
    }

    private long getNotificationTime() {
        String[] dateParts = datePickerButton.getText().toString().split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        String[] time = timeButton.getText().toString().split(":");
        int hour = Integer.parseInt(time[0].trim());
        int minute = Integer.parseInt(time[1].trim());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            Date date1 = sdf.parse(datePickerButton.getText().toString());
            Date date2 = sdf.parse(getTodaysDate());
            LocalTime hourDue = LocalTime.parse(timeButton.getText().toString());
            LocalTime currentTime = LocalTime.parse(getCurrentTime());

            assert date1 != null;

            if (date1.equals(date2) && hourDue.isBefore(currentTime)) {
                return 2;
            }

            if (date1.equals(date2) && hourDue.isAfter(currentTime.minusHours(1)) && hourDue.isBefore(currentTime)) {
                calendar.setTimeInMillis(System.currentTimeMillis());
            } else {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour - 1);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
            }
            return calendar.getTimeInMillis();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return calendar.getTimeInMillis();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Responsibilities to do";
            String desc = "Used for reminding about responsibilities";

            CharSequence name2 = "Zaległe obowiązki";
            String desc2 = "Używane do powiadamiania o zaległych obowiązkach";


            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(desc);

            NotificationChannel channel2 = new NotificationChannel(delayChannelID, name2, importance);
            channel2.setDescription(desc2);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
        }
    }

    public void updateResponsibility() {
        if (checkInput()) {
            db.profileDao().updateResponsibility(respNameET.getText().toString(),
                    checkResponsibilityType(),
                    checkImportanceSelection(),
                    datePickerButton.getText().toString(),
                    timeButton.getText().toString(),
                    descET.getText().toString(),
                    subjectTypeSpinner.getSelectedItem().toString(),
                    db.profileDao().getSubjectId(subjectSpinner.getSelectedItem().toString()),
                    respId);
            int reminderId = db.profileDao().getNotificationId(respId, "Reminder");
            int delayId = db.profileDao().getNotificationId(respId, "Delay");
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(reminderId);
            notificationManager.cancel(delayId);
            db.profileDao().deleteNotificationById(reminderId);
            db.profileDao().deleteNotificationById(delayId);
            checkResponsibilitiesDelay();
            createNotification(respId);
            createDelayNotification(respId);
            returnToInfo();
        } else {
            Toast.makeText(this, "Uzupełnij wszystkie dane!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void checkResponsibilitiesDelay() {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            Date date1 = sdf.parse(datePickerButton.getText().toString());
            Date date2 = sdf.parse(getTodaysDate());

            assert date1 != null;
            if (date1.before(date2)) {
                db.profileDao().markDelayedResp(respId);
            } else if (date1.equals(date2)) {
                LocalTime hourDue = LocalTime.parse(timeButton.getText().toString());
                LocalTime currentTime = LocalTime.parse(getCurrentTime());
                if (currentTime.isAfter(hourDue)) {
                    db.profileDao().markDelayedResp(respId);
                } else {
                    db.profileDao().markUndelayedResp(respId);
                }
            } else {
                db.profileDao().markUndelayedResp(respId);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public Boolean checkDate() {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            Date date1 = sdf.parse(datePickerButton.getText().toString());
            Date date2 = sdf.parse(getTodaysDate());

            assert date1 != null;
            if (date1.before(date2)) {
                return false;
            } else if (date1.equals(date2)) {
                LocalTime hourDue = LocalTime.parse(timeButton.getText().toString());
                LocalTime currentTime = LocalTime.parse(getCurrentTime());
                return !currentTime.isAfter(hourDue);
            } else {
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public void addDatabaseEntry() {
        Responsibility responsibility = setupResponsibility();
        db.profileDao().insertResponsibility(responsibility);
    }

    public void returnToInfo() {
        Intent intent = new Intent(this, ResponsibilityInfo.class);
        Bundle bundle = new Bundle();
        bundle.putInt("respId", respId);
        if (fromCalendar) {
            bundle.putString("calendar", "isCalendar");
        }
        if (fromMenu) {
            bundle.putString("menu", "isMenu");
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public Responsibility setupResponsibility() {
        Responsibility responsibility = new Responsibility();
        responsibility.resp_name = respNameET.getText().toString();
        responsibility.responsibility_type = checkResponsibilityType();
        responsibility.importance = checkImportanceSelection();
        responsibility.date_due = datePickerButton.getText().toString();
        responsibility.hour_due = timeButton.getText().toString();
        responsibility.description = descET.getText().toString();
        responsibility.subject_type = subjectTypeSpinner.getSelectedItem().toString();
        responsibility.subject_id = db.profileDao().getSubjectId(subjectSpinner.getSelectedItem().toString());
        return responsibility;
    }

    public boolean checkInput() {
        System.out.println("SELECTED IMPORTANCE: " + checkImportanceSelection());
        return !respNameET.getText().toString().isEmpty()
                && checkResponsibilityType() != null
                && checkImportanceSelection() != null
                && !datePickerButton.getText().toString().isEmpty()
                && !timeButton.getText().toString().isEmpty();
    }

    public String checkImportanceSelection() {

        String selectedImportance;
        if (normalImportance.isChecked()) {
            selectedImportance = "normal";
        } else if (mediumImportance.isChecked()) {
            selectedImportance = "medium";
        } else if (highImportance.isChecked()) {
            selectedImportance = "high";
        } else {
            selectedImportance = null;
        }
        return selectedImportance;
    }

    public String checkResponsibilityType() {
        String selectedType;

        if (taskRB.isChecked()) {
            selectedType = "Task";
        } else if (testRB.isChecked()) {
            selectedType = "Test";
        } else {
            selectedType = null;
        }

        return selectedType;
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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;

            String date = makeDateString(day, month, year);
            datePickerButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {

        if (isEdit) {
            Intent intent = new Intent(this, ResponsibilityInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("respId", respId);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                Intent intent = new Intent(this, ResponsibilitiesView.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Kliknij jeszcze raz, aby opuścić. Dane nie zostaną zapisane.", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }

    public void returnToView() {
        Intent intent = new Intent(this, ResponsibilitiesView.class);
        startActivity(intent);
    }
}