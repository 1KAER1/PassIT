package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.passit.db.entities.Responsibility;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ResponsibilityInfo extends AppCompatActivity {

    private int respId;
    private boolean fromCalendar, fromMenu = false;
    private MaterialAutoCompleteTextView respNameTV, respTypeTV, dateTV, respDescriptionTV, assignedSubjectTV;
    private TextInputLayout dateDueTIL;
    private AppDatabase db;
    private ImageView importanceCircle;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private Button deleteBtn, editBtn, finishBtn, returnBtn;
    private boolean editedDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibility_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            respId = extras.getInt("respId");
            if (extras.getString("calendar") != null) {
                fromCalendar = true;
            }
            if (extras.getString("menu") != null) {
                fromMenu = true;
            }
        }

        respNameTV = findViewById(R.id.respNameET);
        respTypeTV = findViewById(R.id.respTypeET);
        assignedSubjectTV = findViewById(R.id.assignedSubjectET);
        dateTV = findViewById(R.id.dateDueET);
        respDescriptionTV = findViewById(R.id.noOfSubjectsTV);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);
        finishBtn = findViewById(R.id.markFinishedBtn);
        returnBtn = findViewById(R.id.returnBtn);
        importanceCircle = findViewById(R.id.importanceCircle);
        dateDueTIL = findViewById(R.id.dateDueId);

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
                if (fromCalendar) {
                    bundle.putString("calendar", "isCalendar");
                } else if(fromMenu){
                    bundle.putString("menu", "isMenu");
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromCalendar) {
                    returnToCalendar();
                } else if (fromMenu) {
                    returnToMenu();
                } else {
                    returnToView();
                }
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
                dateDueTIL.setHint("Termin oddania zadania");
                break;
            case "Test":
                respTypeTV.setText("Zaliczenie");
                dateDueTIL.setHint("Data zaliczenia");
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
        if (fromCalendar) {
            returnToCalendar();
        } else if (fromMenu) {
            returnToMenu();
        } else {
            returnToView();
        }
    }

    public void returnToView() {
        Intent intent = new Intent(this, ResponsibilitiesView.class);
        startActivity(intent);
    }

    public void returnToCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("editedDate", responsibilitiesList.get(0).getDate_due());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void returnToMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}