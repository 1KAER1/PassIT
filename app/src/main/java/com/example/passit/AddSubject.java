package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.passit.db.entities.Subject;

public class AddSubject extends AppCompatActivity {

    private Button nextButton;
    private EditText subjectName;
    private EditText ectsPoints;
    private CheckBox lectureCB, exerciseCB, labCB;
    private RadioButton normalImportance, mediumImportance, highImportance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        nextButton = findViewById(R.id.nextBtn);
        subjectName = findViewById(R.id.testNameTV);
        ectsPoints = findViewById(R.id.dateDueTV);
        lectureCB = findViewById(R.id.lectureCB);
        exerciseCB = findViewById(R.id.exerciseCB);
        labCB = findViewById(R.id.labCB);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppDatabase db = AppDatabase.getDbInstance(getApplicationContext());

                String selectedImportance;
                selectedImportance = checkImportanceSelection();

                if (TextUtils.isEmpty(subjectName.getText()) || TextUtils.isEmpty(ectsPoints.getText()) || selectedImportance == null || !checkLessonType()) {
                    Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie dane!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    saveNewSubject(
                            subjectName.getText().toString(),
                            lectureCB.isChecked(),
                            exerciseCB.isChecked(),
                            labCB.isChecked(),
                            Integer.parseInt(ectsPoints.getText().toString()),
                            selectedImportance
                    );
                    returnToSubjects();
                }
            }
        });
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

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        Subject subject = new Subject();
        subject.subject_name = subjectName;
        subject.is_lecture = isLecture;
        subject.is_exercise = isExercise;
        subject.is_lab = isLab;
        subject.ects_points = ectsPoints;
        subject.importance = importance;
        subject.profile_id = db.profileDao().getActiveProfile();
        db.profileDao().insertSubject(subject);

        Toast.makeText(getApplicationContext(), "Dodano do bazy",
                Toast.LENGTH_LONG).show();

        finish();

    }
}