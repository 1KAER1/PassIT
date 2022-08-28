package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            testId = extras.getInt("testId");
        }

        importanceTV = findViewById(R.id.importanceTV);
        testNameTV = findViewById(R.id.testNameTV);
        assignedSubjectTV = findViewById(R.id.assignedSubjectTV);
        dateTV = findViewById(R.id.dateTV);
        testDescriptionTV = findViewById(R.id.testDescription);

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

        String dateWithHour = testsList.get(0).getDate_due() + "    " + testsList.get(0).getHour_due();

        testNameTV.setText(testsList.get(0).getTest_name());
        assignedSubjectTV.setText(db.profileDao().getSubjectName(testsList.get(0).getSubject_id()));
        dateTV.setText(dateWithHour);
        testDescriptionTV.setText(testsList.get(0).getDescription());

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TestsView.class);
        startActivity(intent);
    }
}