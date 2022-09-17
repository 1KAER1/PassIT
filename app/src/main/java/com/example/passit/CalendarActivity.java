package com.example.passit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.passit.db.entities.Responsibility;
import com.example.passit.rvadapters.CalendarActivityRVAdapter;
import com.example.passit.rvadapters.ResponsibilitiesRVAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private CalendarActivityRVAdapter adapter;
    private String selectedDate;
    private boolean specificDate = false;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_activity);

        db = AppDatabase.getDbInstance(this);

        calendar = findViewById(R.id.calendarID);
        recyclerView = findViewById(R.id.respRV);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = extras.getString("editedDate");
            specificDate = true;
            try {
                calendar.setDate(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate)).getTime(), true, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (specificDate) {
            responsibilitiesList = db.profileDao().getResponsibilityWithDate(selectedDate);
        } else {
            responsibilitiesList = db.profileDao().getResponsibilityWithDate(getTodaysDate());
        }
        adapter = new CalendarActivityRVAdapter(responsibilitiesList);
        recyclerView.setAdapter(adapter);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;

                String date = day + "/" + month + "/" + year;
                /*Toast.makeText(getApplicationContext(), "Wybrana data: " + date,
                        Toast.LENGTH_SHORT).show();*/
                responsibilitiesList = db.profileDao().getResponsibilityWithDate(date);
                adapter = new CalendarActivityRVAdapter(responsibilitiesList);
                recyclerView.setAdapter(adapter);
            }
        });
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

    @Override
    public void onBackPressed() {
        returnToView();
    }

    public void returnToView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}