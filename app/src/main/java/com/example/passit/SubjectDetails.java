package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.LessonDate;
import com.example.passit.db.entities.Subject;
import com.example.passit.rvadapters.ExerciseInfoRVAdapter;
import com.example.passit.rvadapters.LabInfoRVAdapter;
import com.example.passit.rvadapters.LectureInfoRVAdapter;
import com.example.passit.rvadapters.SummaryRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetails extends AppCompatActivity {

    private int subjectId;
    private AppDatabase db;
    private CheckBox lectureCB, exerciseCB, labCB;
    private TextView importance, subjectName, ects,
            lectureLecturer, exerciseLecturer, labLecturer,
            lectureStartingWeek, exerciseStartingWeek, labStartingWeek,
            lectureEndWeek, exerciseEndWeek, labEndWeek;
    private RecyclerView lectureRV, exerciseRV, labRV;
    private List<Subject> subjectsList = new ArrayList<>();

    private List<Lesson> lectureList = new ArrayList<>();
    private List<LessonDate> lectureDatesList = new ArrayList<>();
    private LectureInfoRVAdapter lectureInfoRVAdapter;

    private List<Lesson> exerciseList = new ArrayList<>();
    private List<LessonDate> exerciseDatesList = new ArrayList<>();
    private ExerciseInfoRVAdapter exerciseInfoRVAdapter;

    private List<Lesson> labList = new ArrayList<>();
    private List<LessonDate> labDatesList = new ArrayList<>();
    private LabInfoRVAdapter labInfoRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getInt("subjectId");
        }

        importance = findViewById(R.id.importanceTV);
        subjectName = findViewById(R.id.subjectNameTV);
        ects = findViewById(R.id.ectsTV);
        lectureCB = findViewById(R.id.lectureCB);
        exerciseCB = findViewById(R.id.exerciseCB);
        labCB = findViewById(R.id.labCB);
        /*lectureLecturer = findViewById(R.id.lectureLecturerTV);
        exerciseLecturer = findViewById(R.id.exerciseLecturerTV);
        labLecturer = findViewById(R.id.labLecturerTV);

        lectureStartingWeek = findViewById(R.id.startingWeekTV);
        exerciseStartingWeek = findViewById(R.id.startingWeekTV1);
        labStartingWeek = findViewById(R.id.startingWeekTV11);

        lectureEndWeek = findViewById(R.id.lastWeekTV);
        exerciseEndWeek = findViewById(R.id.lastWeekTV1);
        labEndWeek = findViewById(R.id.lastWeekTV11);*/

        lectureRV = findViewById(R.id.lectureRV);
        exerciseRV = findViewById(R.id.exerciseRV);
        labRV = findViewById(R.id.labRV);

        db = AppDatabase.getDbInstance(this);

        Toast.makeText(this, "Przesłane id przedmiotu: " + subjectId,
                Toast.LENGTH_SHORT).show();

        subjectsList = db.profileDao().getSubjectWithId(subjectId);

        switch (subjectsList.get(0).getImportance()) {
            case "normal":
                importance.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                importance.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                importance.setBackgroundResource(R.color.highImportance);
                break;
        }
        subjectName.setText(subjectsList.get(0).getSubject_name());
        ects.setText(String.valueOf(subjectsList.get(0).getEcts_points()));

        if (subjectsList.get(0).isIs_lecture()){
            lectureCB.setChecked(true);
        }

        if (subjectsList.get(0).isIs_exercise()){
            exerciseCB.setChecked(true);
        }

        if (subjectsList.get(0).isIs_lab()){
            labCB.setChecked(true);
        }

        /*if (subjectsList.get(0).isIs_lecture()) {
            lectureList = db.profileDao().getLessonWithId(subjectId, "Lecture");
            //lectureDatesList = db.profileDao().getLessonDate(lectureList.get(0).getLesson_id());

            lectureLecturer.setText(lectureList.get(0).getLecturer_name());
            lectureStartingWeek.setText(String.valueOf(lectureList.get(0).getStarting_week()));
            lectureEndWeek.setText(String.valueOf(lectureList.get(0).getEnding_week()));

            lectureInfoRVAdapter = new LectureInfoRVAdapter(lectureDatesList);
            lectureRV.setAdapter(lectureInfoRVAdapter);
        }

        if (subjectsList.get(0).isIs_exercise()) {
            exerciseList = db.profileDao().getLessonWithId(subjectId, "Exercise");
            exerciseDatesList = db.profileDao().getLessonDate(exerciseList.get(0).getLesson_id());

            exerciseLecturer.setText(exerciseList.get(0).getLecturer_name());
            exerciseStartingWeek.setText(String.valueOf(exerciseList.get(0).getStarting_week()));
            exerciseEndWeek.setText(String.valueOf(exerciseList.get(0).getEnding_week()));

            exerciseInfoRVAdapter = new ExerciseInfoRVAdapter(exerciseDatesList);
            exerciseRV.setAdapter(exerciseInfoRVAdapter);
        }

        if (subjectsList.get(0).isIs_lab()) {
            labList = db.profileDao().getLessonWithId(subjectId, "Lab");
            labDatesList = db.profileDao().getLessonDate(labList.get(0).getLesson_id());

            labLecturer.setText(labList.get(0).getLecturer_name());
            labStartingWeek.setText(String.valueOf(labList.get(0).getStarting_week()));
            labEndWeek.setText(String.valueOf(labList.get(0).getEnding_week()));

            labInfoRVAdapter = new LabInfoRVAdapter(labDatesList);
            labRV.setAdapter(labInfoRVAdapter);
        }*/

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }
}