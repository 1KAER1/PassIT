package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Subject;

public class AddSubject extends AppCompatActivity {

    private Button nextButton;
    private EditText subjectName;
    private EditText ectsPoints;
    private CheckBox lectureCB, exerciseCB, labCB;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private RadioGroup importanceRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        nextButton = findViewById(R.id.nextBtn);
        subjectName = findViewById(R.id.subjectName);
        ectsPoints = findViewById(R.id.ectsPoints);
        lectureCB = findViewById(R.id.lectureCB);
        exerciseCB = findViewById(R.id.exerciseCB);
        labCB = findViewById(R.id.labCB);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        importanceRadioGroup = findViewById(R.id.importanceRadioGroup);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedImportance = null;
                if (normalImportance.isChecked()) {
                    selectedImportance = "normal";
                } else if (mediumImportance.isChecked()) {
                    selectedImportance = "medium";
                } else if (highImportance.isChecked()) {
                    selectedImportance = "high";
                } else {
                    Toast.makeText(getApplicationContext(), "No importance Selected!",
                            Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), "Selected importance: " + selectedImportance,
                        Toast.LENGTH_SHORT).show();


                saveNewSubject(
                        subjectName.getText().toString(),
                        lectureCB.isChecked(),
                        exerciseCB.isChecked(),
                        labCB.isChecked(),
                        Integer.parseInt(ectsPoints.getText().toString()),
                        selectedImportance
                );
                nextSite();
            }
        });
    }

    public void nextSite() {

        Intent intent = new Intent(this, AddClass.class);
        startActivity(intent);
    }

    public void saveNewSubject(String subjectName, boolean isLecture, boolean isExercise, boolean isLab, int ectsPoints, String importance) {

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        Profile profile = new Profile();
        profile.profile_name = "Informatyka";
        profile.semester = 7;
        db.profileDao().insertProfile(profile);

        Subject subject = new Subject();
        subject.subject_name = subjectName;
        subject.is_lecture = isLecture;
        subject.is_exercise = isExercise;
        subject.is_lab = isLab;
        subject.ect_points = ectsPoints;
        subject.importance = importance;
        subject.profile_id = 1;
        db.profileDao().insertSubject(subject);

        Toast.makeText(getApplicationContext(), "Dodano do bazy",
                Toast.LENGTH_LONG).show();

        finish();

    }
}