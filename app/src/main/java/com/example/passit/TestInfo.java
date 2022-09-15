package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.passit.db.entities.Test;

import java.util.ArrayList;
import java.util.List;

public class TestInfo extends AppCompatActivity {

    private int testId;
    private TextView testNameTV, assignedSubjectTV, dateTV, testDescriptionTV, importanceTV;
    private AppDatabase db;
    private List<Test> testsList = new ArrayList<>();
    private Button deleteBtn, editBtn, finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            testId = extras.getInt("testId");
        }

        importanceTV = findViewById(R.id.importanceTV);
        testNameTV = findViewById(R.id.subjectTV);
        assignedSubjectTV = findViewById(R.id.assignedSubjectTV);
        dateTV = findViewById(R.id.dateTV);
        testDescriptionTV = findViewById(R.id.testDescription);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);
        finishBtn = findViewById(R.id.markFinishedBtn);

        db = AppDatabase.getDbInstance(this);

        testsList = db.profileDao().getTestWithId(testId);

        switch (testsList.get(0).getImportance()) {
            case "normal":
                importanceTV.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                importanceTV.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                importanceTV.setBackgroundResource(R.color.highImportance);
                break;
        }

        if (db.profileDao().getTestState(testId)) {
            finishBtn.setBackgroundResource(R.drawable.finish_button);
        } else {
            finishBtn.setBackgroundResource(R.drawable.unfinished_button);
        }

        setText();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.profileDao().deleteTest(testId);
                returnToView();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (db.profileDao().getTestState(testId)) {
                    finishBtn.setBackgroundResource(R.drawable.unfinished_button);
                    db.profileDao().setUnfinishedTest(testId);
                } else {
                    finishBtn.setBackgroundResource(R.drawable.finish_button);
                    db.profileDao().setPassedTest(testId);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTest.class);
                Bundle bundle = new Bundle();
                bundle.putInt("testId", testId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void setText() {
        String dateWithHour = testsList.get(0).getDate_due() + "  " + testsList.get(0).getHour_due();

        testNameTV.setText(testsList.get(0).getTest_name());
        assignedSubjectTV.setText(db.profileDao().getSubjectName(testsList.get(0).getSubject_id()));
        dateTV.setText(dateWithHour);
        testDescriptionTV.setText(testsList.get(0).getDescription());
    }

    @Override
    public void onBackPressed() {
        returnToView();
    }

    public void returnToView() {
        Intent intent = new Intent(this, TestsView.class);
        startActivity(intent);
    }
}