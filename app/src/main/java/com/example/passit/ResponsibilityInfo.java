package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class ResponsibilityInfo extends AppCompatActivity {

    private int respId;
    private MaterialAutoCompleteTextView respNameTV, respTypeTV, dateTV, respDescriptionTV, assignedSubjectTV;
    private AppDatabase db;
    private ImageView importanceCircle;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private Button deleteBtn, editBtn, finishBtn, returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibility_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            respId = extras.getInt("respId");
        }

        respNameTV = findViewById(R.id.respNameET);
        respTypeTV = findViewById(R.id.respTypeET);
        assignedSubjectTV = findViewById(R.id.assignedSubjectET);
        dateTV = findViewById(R.id.dateDueET);
        respDescriptionTV = findViewById(R.id.descriptionET);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);
        finishBtn = findViewById(R.id.markFinishedBtn);
        returnBtn = findViewById(R.id.returnBtn);
        importanceCircle = findViewById(R.id.importanceCircle);

        db = AppDatabase.getDbInstance(this);

        responsibilitiesList = db.profileDao().getResponsibilityWithId(respId);

        if (db.profileDao().getResponsibilityState(respId)) {
            finishBtn.setBackgroundResource(R.drawable.finish_button);
        } else {
            finishBtn.setBackgroundResource(R.drawable.unfinished_button);
        }

        fillInfo();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.profileDao().deleteResponsibility(respId);
                returnToView();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (db.profileDao().getResponsibilityState(respId)) {
                    finishBtn.setBackgroundResource(R.drawable.unfinished_button);
                    db.profileDao().setUnfinishedResponsibility(respId);
                } else {
                    finishBtn.setBackgroundResource(R.drawable.finish_button);
                    db.profileDao().setFinishedResponsibility(respId);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddResponsibility.class);
                Bundle bundle = new Bundle();
                bundle.putInt("respId", respId);
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

    public void fillInfo() {
        String dateWithHour = responsibilitiesList.get(0).getDate_due() + "  " + responsibilitiesList.get(0).getHour_due();
        String assignedSubject = db.profileDao().getSubjectName(responsibilitiesList.get(0).getSubject_id()) + "\n("
                + responsibilitiesList.get(0).getSubject_type() + ")";

        respNameTV.setText(responsibilitiesList.get(0).getResp_name());
        switch (responsibilitiesList.get(0).getResponsibility_type()) {
            case "Task":
                respTypeTV.setText("Zadanie");
                break;
            case "Test":
                respTypeTV.setText("Zaliczenie");
                break;
        }

        switch (responsibilitiesList.get(0).getImportance()) {
            case "normal":
                importanceCircle.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                importanceCircle.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                importanceCircle.setBackgroundResource(R.color.highImportance);
                break;
        }
        assignedSubjectTV.setText(assignedSubject);
        dateTV.setText(dateWithHour);
        respDescriptionTV.setText(responsibilitiesList.get(0).getDescription());
    }

    @Override
    public void onBackPressed() {
        returnToView();
    }

    public void returnToView() {
        Intent intent = new Intent(this, ResponsibilitiesView.class);
        startActivity(intent);
    }
}