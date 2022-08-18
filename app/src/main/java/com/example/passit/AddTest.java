package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.passit.db.entities.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTest extends AppCompatActivity {

    private EditText taskName, taskDescription;
    private Spinner subjectSpinner;
    private Button datePickerButton, nextButton, timeButton;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private RadioGroup importanceRadioGroup;
    private int hour, minute;
    private List<String> subjectsList = new ArrayList<>();
    private AppDatabase db;
    private DatePickerDialog datePickerDialog;
    private String selectedImportance = null;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        initDatePicker();

        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        importanceRadioGroup = findViewById(R.id.importanceRadioGroup);
        taskName = findViewById(R.id.taskNameET);
        taskDescription = findViewById(R.id.taskDescription);
        subjectSpinner = findViewById(R.id.spinnerPicker);
        datePickerButton = findViewById(R.id.datePicker);
        timeButton = findViewById(R.id.timePicker);
        nextButton = findViewById(R.id.nextBtn);

        db = AppDatabase.getDbInstance(this);

        subjectsList = db.profileDao().getAllSubjectsNames();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_layout, subjectsList);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
        subjectSpinner.setAdapter(adapter);

        datePickerButton.setHint(getTodaysDate());
        timeButton.setHint(getCurrentTime());

        timeButton.setOnClickListener(view -> popTimePicker());
        datePickerButton.setOnClickListener(this::openDatePicker);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTestToDatabase();
            }
        });
    }

    public void addTestToDatabase() {
        if (checkInput()) {
            addDatabaseEntry();
            returnToTestView();
        } else {
            Toast.makeText(this, "Uzupełnij wszystkie dane!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void addDatabaseEntry() {
        Test test = new Test();
        test.test_name = taskName.getText().toString();
        test.importance = checkImportanceSelection();
        test.date_due = datePickerButton.getText().toString();
        test.hour_due = timeButton.getText().toString();
        test.description = taskDescription.getText().toString();
        test.subject_id = db.profileDao().getSubjectId(subjectSpinner.getSelectedItem().toString());
        db.profileDao().insertTest(test);
    }

    public boolean checkInput() {
        System.out.println("SELECTED IMPORTANCE: " + checkImportanceSelection());
        if (!taskName.getText().toString().isEmpty()
                && checkImportanceSelection() != null
                && !taskDescription.getText().toString().isEmpty()
                && !datePickerButton.getText().toString().isEmpty()
                && !timeButton.getText().toString().isEmpty()) {
            return true;
        }

        return false;
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
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datePickerButton.setText(date);
            }
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
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(this, TasksView.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Kliknij jeszcze raz, aby opuścić. Dane nie zostaną zapisane.", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void returnToTestView() {
        Intent intent = new Intent(this, TasksView.class);
        startActivity(intent);
    }
}