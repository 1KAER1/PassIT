package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.rvadapters.ResponsibilitiesRVAdapter;
import com.example.passit.rvadapters.TasksViewRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResponsibilitiesView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private FloatingActionButton addNewBtn;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private ResponsibilitiesRVAdapter adapter;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibilities_view);

        addNewBtn = findViewById(R.id.addNewBtn);
        recyclerView = findViewById(R.id.responsibilitiesRV);
        normalImportance = findViewById(R.id.normalImportance);
        mediumImportance = findViewById(R.id.mediumImportance);
        highImportance = findViewById(R.id.highImportance);
        searchBar = findViewById(R.id.searchBar);

        db = AppDatabase.getDbInstance(this);

        responsibilitiesList = db.profileDao().getAllResponsibilities();
        checkResponsibilitiesDelay();

        subjectList = db.profileDao().getAllSubjects();

        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
        recyclerView.setAdapter(adapter);

        addNewBtn.setOnClickListener(view -> {
            if (subjectList.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Dodaj przedmioty, aby móc dodawać zadania!",
                        Toast.LENGTH_SHORT).show();
            } else {
                openAddNewTask();
            }
        });
    }

    public void checkResponsibilitiesDelay() {
        try {
            for (Responsibility resp : responsibilitiesList) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
                Date date1 = sdf.parse(resp.getDate_due());
                Date date2 = sdf.parse(getTodaysDate());

                assert date1 != null;
                if (date1.before(date2)) {
                    db.profileDao().markDelayedResp(resp.getResp_id());
                } else if (date1.equals(date2)) {
                    LocalTime hourDue = LocalTime.parse(resp.getHour_due());
                    LocalTime currentTime = LocalTime.parse(getCurrentTime());
                    if (currentTime.isAfter(hourDue)) {
                        db.profileDao().markDelayedResp(resp.getResp_id());
                    }
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
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

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    public void openAddNewTask() {
        Intent intent = new Intent(this, AddResponsibility.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}