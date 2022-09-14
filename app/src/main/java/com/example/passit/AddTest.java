package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passit.db.entities.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTest extends AppCompatActivity {

    private EditText testName, taskDescription;
    private TextView headlineTV;
    private Spinner subjectSpinner, subjectTypeSpinner;
    private Button datePickerButton, nextButton, timeButton;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private RadioGroup importanceRadioGroup;
    private int hour, minute;
    private List<String> subjectsList = new ArrayList<>();
    private List<String> subjectTypeList = new ArrayList<>();
    private List<Test> testList = new ArrayList<>();
    private AppDatabase db;
    private DatePickerDialog datePickerDialog;
    private String selectedImportance = null;
    private long pressedTime;
    private int testId;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        db = AppDatabase.getDbInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEdit = true;
            testId = extras.getInt("testId");
            testList = db.profileDao().getTestWithId(testId);
        }

        initDatePicker();

        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        importanceRadioGroup = findViewById(R.id.importanceRadioGroup);
        testName = findViewById(R.id.testNameTV);
        taskDescription = findViewById(R.id.testDescription);
        subjectSpinner = findViewById(R.id.assignedSubjectTV);
        subjectTypeSpinner = findViewById(R.id.subjectTypeSpinner);
        datePickerButton = findViewById(R.id.datePicker);
        timeButton = findViewById(R.id.timePicker);
        nextButton = findViewById(R.id.nextBtn);
        headlineTV = findViewById(R.id.headlineTV);

        subjectsList = db.profileDao().getAllSubjectsNames();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_layout, subjectsList);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
        subjectSpinner.setAdapter(adapter);

        if (isEdit) {
            nextButton.setText("ZAKTUALIZUJ");
            headlineTV.setText("Edytuj test");
            int selectionPosition = adapter.getPosition(db.profileDao().getSubjectName(testList.get(0).getSubject_id()));
            subjectSpinner.setSelection(selectionPosition);
            testName.setText(testList.get(0).getTest_name());
            datePickerButton.setText(testList.get(0).getDate_due());
            timeButton.setText(testList.get(0).getHour_due());
            taskDescription.setText(testList.get(0).getDescription());

            switch (testList.get(0).getImportance()) {
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
                    int selectionPosition2 = subjectTypeAdapter.getPosition(testList.get(0).getSubject_type());
                    subjectTypeSpinner.setSelection(selectionPosition2);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        datePickerButton.setHint(getTodaysDate());
        timeButton.setHint(getCurrentTime());

        timeButton.setOnClickListener(view -> popTimePicker());
        datePickerButton.setOnClickListener(this::openDatePicker);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    updateTest();
                } else {
                    addTestToDatabase();
                }
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

    public void updateTest() {
        if (checkInput()) {
            db.profileDao().updateTest(testName.getText().toString(),
                    checkImportanceSelection(),
                    datePickerButton.getText().toString(),
                    timeButton.getText().toString(),
                    taskDescription.getText().toString(),
                    subjectTypeSpinner.getSelectedItem().toString(),
                    db.profileDao().getSubjectId(subjectSpinner.getSelectedItem().toString()),
                    testId);
            Toast.makeText(this, "Test Name: " + testName.getText().toString(),
                    Toast.LENGTH_SHORT).show();
            returnToInfo();

        } else {
            Toast.makeText(this, "Uzupełnij wszystkie dane!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void addDatabaseEntry() {
        Test test = new Test();
        test.test_name = testName.getText().toString();
        test.importance = checkImportanceSelection();
        test.date_due = datePickerButton.getText().toString();
        test.hour_due = timeButton.getText().toString();
        test.description = taskDescription.getText().toString();
        test.subject_type = subjectTypeSpinner.getSelectedItem().toString();
        test.subject_id = db.profileDao().getSubjectId(subjectSpinner.getSelectedItem().toString());
        db.profileDao().insertTest(test);
    }

    public void returnToInfo() {
        Intent intent = new Intent(this, TestInfo.class);
        Bundle bundle = new Bundle();
        bundle.putInt("testId", testId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean checkInput() {
        System.out.println("SELECTED IMPORTANCE: " + checkImportanceSelection());
        return !testName.getText().toString().isEmpty()
                && checkImportanceSelection() != null
                && !taskDescription.getText().toString().isEmpty()
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
            Intent intent = new Intent(this, TestsView.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Kliknij jeszcze raz, aby opuścić. Dane nie zostaną zapisane.", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void returnToTestView() {
        Intent intent = new Intent(this, TestsView.class);
        startActivity(intent);
    }
}