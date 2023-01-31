package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.passit.db.AppDatabase;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;
import com.example.passit.helpers.NotificationSender;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetails extends AppCompatActivity {

    private int subjectId;
    private AppDatabase db;
    private CheckBox lectureCB, exerciseCB, labCB;
    private TextView importance, subjectName, ects;
    private List<Subject> subjectsList = new ArrayList<>();
    private Button deleteBtn, editBtn, finishBtn, returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);
        finishBtn = findViewById(R.id.markFinishedBtn);
        returnBtn = findViewById(R.id.returnBtn);

        db = AppDatabase.getDbInstance(this);

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

        if (db.profileDao().getSubjectState(subjectId)) {
            finishBtn.setBackgroundResource(R.drawable.finish_button);
        } else {
            finishBtn.setBackgroundResource(R.drawable.unfinished_button);
        }

        subjectName.setText(subjectsList.get(0).getSubject_name());
        ects.setText(String.valueOf(subjectsList.get(0).getEcts_points()));

        if (subjectsList.get(0).isIs_lecture()) {
            lectureCB.setChecked(true);
        }

        if (subjectsList.get(0).isIs_exercise()) {
            exerciseCB.setChecked(true);
        }

        if (subjectsList.get(0).isIs_lab()) {
            labCB.setChecked(true);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Usuń przedmiot");
                builder.setMessage("Czy na pewno chcesz usunąć przedmiot " + subjectsList.get(0).getSubject_name() + "? Usunięcie przedmiotu usunie wszystkie przypisane do niego zadania!");
                builder.setCancelable(false);
                builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                    NotificationSender notificationSender = new NotificationSender(view.getContext());
                    List<Responsibility> responsibilities = db.profileDao().getResponsibilityWithSubjectId(subjectsList.get(0).subject_id);

                    for (Responsibility resp : responsibilities) {
                        notificationSender.cancelNotification(db.profileDao().getNotificationId(resp.getResp_id(), "Reminder"));
                        notificationSender.cancelNotification(db.profileDao().getNotificationId(resp.getResp_id(), "Delay"));
                        db.profileDao().deleteNotificationById(db.profileDao().getNotificationId(resp.getResp_id(), "Reminder"));
                        db.profileDao().deleteNotificationById(db.profileDao().getNotificationId(resp.getResp_id(), "Delay"));
                        db.profileDao().deleteResponsibility(resp.getResp_id());
                    }

                    db.profileDao().deleteSubject(subjectId);
                    returnToView();
                    dialog.dismiss();
                });

                builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (db.profileDao().getSubjectState(subjectId)) {
                    finishBtn.setBackgroundResource(R.drawable.unfinished_button);
                    db.profileDao().setSubjectInProgress(subjectId);
                } else {
                    finishBtn.setBackgroundResource(R.drawable.finish_button);
                    db.profileDao().setPassedSubject(subjectId);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddSubject.class);
                Bundle bundle = new Bundle();
                bundle.putInt("subjectId", subjectId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToView();
            }
        });


    }

    @Override
    public void onBackPressed() {
        returnToView();
    }

    public void returnToView() {
        Intent intent = new Intent(this, SubjectsView.class);
        startActivity(intent);
    }
}