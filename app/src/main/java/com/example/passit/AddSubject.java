package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.passit.db.AppDatabase;
import com.example.passit.db.entities.Subject;
import com.example.passit.helpers.InputFilterMinMax;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddSubject extends AppCompatActivity {

    private int subjectId;
    private boolean isEdit = false;
    private Button nextButton;
    private TextInputEditText subjectName, ectsPointsET;
    private CheckBox lectureCB, exerciseCB, labCB;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private List<Subject> subjectList = new ArrayList<>();
    private AppDatabase db;
    private long pressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        setTitle("Dodaj nowy przedmiot");

        db = AppDatabase.getDbInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEdit = true;
            subjectId = extras.getInt("subjectId");
            subjectList = db.profileDao().getSubjectWithId(subjectId);
        }

        nextButton = findViewById(R.id.nextBtn);
        subjectName = findViewById(R.id.noteTitle);
        ectsPointsET = findViewById(R.id.ectsPointsTV);
        lectureCB = findViewById(R.id.lectureCB);
        exerciseCB = findViewById(R.id.exerciseCB);
        labCB = findViewById(R.id.labCB);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);

        ectsPointsET.setFilters(new android.text.InputFilter[]{new InputFilterMinMax("0", "15")});

        subjectName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        ectsPointsET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        if (isEdit) {
            setTitle("Edytuj przedmiot");
            subjectName.setText(subjectList.get(0).getSubject_name());
            ectsPointsET.setText(String.valueOf(subjectList.get(0).getEcts_points()));
            nextButton.setText("ZAKTUALIZUJ");
            if (subjectList.get(0).isIs_lecture()) {
                lectureCB.setChecked(true);
            }
            if (subjectList.get(0).isIs_exercise()) {
                exerciseCB.setChecked(true);
            }
            if (subjectList.get(0).isIs_lab()) {
                labCB.setChecked(true);
            }

            switch (subjectList.get(0).getImportance()) {
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


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedImportance;
                selectedImportance = checkImportanceSelection();

                if (TextUtils.isEmpty(subjectName.getText()) || TextUtils.isEmpty(ectsPointsET.getText()) || selectedImportance == null || !checkLessonType()) {
                    Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie dane!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (isEdit) {
                        updateSubject();
                    } else {
                        saveNewSubject(
                                subjectName.getText().toString(),
                                lectureCB.isChecked(),
                                exerciseCB.isChecked(),
                                labCB.isChecked(),
                                Integer.parseInt(ectsPointsET.getText().toString()),
                                selectedImportance
                        );
                        returnToSubjects();
                    }
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void returnToSubjects() {

        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }

    public boolean checkLessonType() {
        return lectureCB.isChecked() || exerciseCB.isChecked() || labCB.isChecked();
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

    public void saveNewSubject(String subjectName, boolean isLecture, boolean isExercise, boolean isLab, int ectsPoints, String importance) {
        Subject subject = new Subject();
        subject.subject_name = subjectName.replaceAll("\n", " ").replaceAll(" +", " ").trim();
        subject.is_lecture = isLecture;
        subject.is_exercise = isExercise;
        subject.is_lab = isLab;
        subject.ects_points = ectsPoints;
        subject.importance = importance;
        subject.profile_id = db.profileDao().getActiveProfile();
        db.profileDao().insertSubject(subject);
        finish();
    }

    public void updateSubject() {
        db.profileDao().updateSubject(subjectName.getText().toString().replaceAll("\n", " ").replaceAll(" +", " ").trim(),
                checkImportanceSelection(),
                ectsPointsET.getText().toString(),
                lectureCB.isChecked(),
                exerciseCB.isChecked(),
                labCB.isChecked(),
                subjectId);

        Toast.makeText(getApplicationContext(), "Zaktualizowane przedmiot!",
                Toast.LENGTH_LONG).show();

        returnToInfo();
    }

    @Override
    public void onBackPressed() {

        if (isEdit) {
            Intent intent = new Intent(this, SubjectDetails.class);
            Bundle bundle = new Bundle();
            bundle.putInt("subjectId", subjectId);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                Intent intent = new Intent(this, SubjectsView.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Kliknij jeszcze raz, aby opuścić. Dane nie zostaną zapisane.", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }

    public void returnToView() {
        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }

    public void returnToInfo() {
        Intent intent = new Intent(this, SubjectDetails.class);
        Bundle bundle = new Bundle();
        bundle.putInt("subjectId", subjectId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}