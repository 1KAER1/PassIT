package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;
import com.example.passit.db.entities.Task;
import com.example.passit.rvadapters.ResponsibilitiesRVAdapter;
import com.example.passit.rvadapters.TasksViewRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ResponsibilitiesView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private FloatingActionButton addNewBtn;
    private Spinner sortSpinner;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private ResponsibilitiesRVAdapter adapter;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();
    private AppDatabase db;
    @SuppressLint("SimpleDateFormat")
    final DateFormat format = new SimpleDateFormat("d/MM/yyyy");

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
        sortSpinner = findViewById(R.id.sortSpinner);

        db = AppDatabase.getDbInstance(this);

        subjectList = db.profileDao().getAllSubjects();

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.responsibilitiesSortArray, R.layout.custom_spinner_layout);
        arrayAdapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
        sortSpinner.setAdapter(arrayAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String sortType = sortSpinner.getSelectedItem().toString();

                responsibilitiesList.clear();

                switch (position) {
                    case 0:
                        responsibilitiesList = db.profileDao().getAllUnfinishedResponsibilities();
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 1:
                        responsibilitiesList = db.profileDao().getHighImportanceResponsibilities();
                        responsibilitiesList.addAll(db.profileDao().getMediumImportanceResponsibilities());
                        responsibilitiesList.addAll(db.profileDao().getNormalImportanceResponsibilities());
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 2:
                        responsibilitiesList = db.profileDao().getNormalImportanceResponsibilities();
                        responsibilitiesList.addAll(db.profileDao().getMediumImportanceResponsibilities());
                        responsibilitiesList.addAll(db.profileDao().getHighImportanceResponsibilities());
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 3:
                        responsibilitiesList = db.profileDao().getAllUnfinishedResponsibilities();
                        sortRespDates(responsibilitiesList);
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 4:
                        responsibilitiesList = db.profileDao().getAllUnfinishedResponsibilities();
                        sortRespDates(responsibilitiesList);
                        Collections.reverse(responsibilitiesList);
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 5:
                        responsibilitiesList = db.profileDao().getHighImportanceResponsibilities();
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 6:
                        responsibilitiesList = db.profileDao().getMediumImportanceResponsibilities();
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                    case 7:
                        responsibilitiesList = db.profileDao().getNormalImportanceResponsibilities();
                        checkResponsibilitiesDelay();
                        adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                        recyclerView.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    public void sortRespDates(List<Responsibility> responsibilityLisT) {
        responsibilityLisT.sort((o1, o2) -> {
            try {
                return Objects.requireNonNull(format.parse(o1.date_due)).compareTo(format.parse(o2.date_due));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
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