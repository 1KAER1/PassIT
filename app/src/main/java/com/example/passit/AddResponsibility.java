package com.example.passit;

import static com.example.passit.notificationbrodcasts.ReminderBroadcast.channelID;
import static com.example.passit.notificationbrodcasts.ReminderBroadcast.delayChannelID;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private TextInputEditText respNameET, descET;
    private Spinner subjectSpinner, subjectTypeSpinner;
    private Button datePickerButton, nextButton, timeButton, filePicker;
    private RadioButton normalImportance, mediumImportance, highImportance, taskRB, testRB;
    private NotificationSender notificationSender;
    private int hour, minute;
    private String savedFileUri;
    private List<String> subjectsList = new ArrayList<>();
    private List<String> subjectTypeList = new ArrayList<>();
    private List<Responsibility> respList = new ArrayList<>();
    private AppDatabase db;
    private DatePickerDialog datePickerDialog;
    private long pressedTime;
    private int respId;
    private boolean isEdit, fromCalendar, fromMenu = false;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_responsibility);

        setTitle("Nowy obowiązek");
        notificationSender = new NotificationSender(this);

        db = AppDatabase.getDbInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEdit = true;
            setTitle("Edytuj obowiązek");
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
        respNameET = findViewById(R.id.respName);
        descET = findViewById(R.id.respDesc);
        subjectSpinner = findViewById(R.id.assignedSubjectTV);
        subjectTypeSpinner = findViewById(R.id.subjectTypeSpinner);
        datePickerButton = findViewById(R.id.datePicker);
        filePicker = findViewById(R.id.filePicker);
        timeButton = findViewById(R.id.timePicker);
        nextButton = findViewById(R.id.nextBtn);
        taskRB = findViewById(R.id.taskRB);
        testRB = findViewById(R.id.testRB);

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();

                        if (data != null) {
                            Uri fileUri = data.getData();
                            savedFileUri = fileUri.toString();
                            String path = fileUri.getPath();
                        }
                    }
                }
        );

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
            savedFileUri = respList.get(0).getFileUri();
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
        filePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddResponsibility.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddResponsibility.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    selectFile();
                }
            }
        });

        respNameET.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        descET.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        nextButton.setOnClickListener(view -> {
            if (isEdit) {
                updateResponsibility();
            } else {
                addResponsibilityToDatabase();
            }
        });
    }

    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            Toast.makeText(getApplicationContext(), "Odmówiono dostępu do plików", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addResponsibilityToDatabase() {
        if (checkInput() && checkDate()) {
            addDatabaseEntry();
            notificationSender.sendNotification(getNotificationTime("Reminder"), db.profileDao().getLastRespId(), "Reminder");
            notificationSender.sendNotification(getNotificationTime("Delay"), db.profileDao().getLastRespId(), "Delay");
            returnToView();
        } else {
            Toast.makeText(this, "Niepoprawne dane!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private long getNotificationTime(String notificationType) {
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

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            assert date1 != null;
            if (notificationType.equals("Reminder")) {
                calendar.set(Calendar.HOUR_OF_DAY, hour - 1);
            } else if (notificationType.equals("Delay")) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return calendar.getTimeInMillis();
    }

    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Przypomnienie o obowiązkach";
            String desc = "Used for reminding about responsibilities";
            CharSequence name2 = "Zaległe obowiązki";
            String desc2 = "Używane do powiadamiania o zaległych obowiązkach";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(desc);
            NotificationChannel channel2 = new NotificationChannel(delayChannelID, name2, importance);
            channel2.setDescription(desc2);

            List<NotificationChannel> channels = new ArrayList<>();
            channels.add(channel);
            channels.add(channel2);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannels(channels);
        }
    }

    public void updateResponsibility() {
        if (checkInput()) {
            db.profileDao().updateResponsibility(
                    respNameET.getText().toString().trim(),
                    checkResponsibilityType(),
                    checkImportanceSelection(),
                    datePickerButton.getText().toString().trim(),
                    timeButton.getText().toString().trim(),
                    descET.getText().toString().trim(),
                    subjectTypeSpinner.getSelectedItem().toString(),
                    savedFileUri,
                    db.profileDao().getSubjectId(subjectSpinner.getSelectedItem().toString()),
                    respId);
            int reminderId = db.profileDao().getNotificationId(respId, "Reminder");
            int delayId = db.profileDao().getNotificationId(respId, "Delay");
            notificationSender.cancelNotification(reminderId);
            notificationSender.cancelNotification(delayId);
            db.profileDao().deleteNotificationById(reminderId);
            db.profileDao().deleteNotificationById(delayId);
            checkResponsibilitiesDelay();
            notificationSender.sendNotification(getNotificationTime("Reminder"), respId, "Reminder");
            notificationSender.sendNotification(getNotificationTime("Delay"), respId, "Delay");
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
        responsibility.resp_name = respNameET.getText().toString().trim();
        responsibility.responsibility_type = checkResponsibilityType();
        responsibility.importance = checkImportanceSelection();
        responsibility.date_due = datePickerButton.getText().toString();
        responsibility.hour_due = timeButton.getText().toString();
        responsibility.description = descET.getText().toString().trim();
        responsibility.fileUri = savedFileUri;
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