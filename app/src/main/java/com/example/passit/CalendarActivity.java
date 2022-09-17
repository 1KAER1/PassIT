package com.example.passit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.passit.db.entities.Responsibility;
import com.example.passit.rvadapters.ResponsibilitiesRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private ResponsibilitiesRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_activity);

        db = AppDatabase.getDbInstance(this);

        calendar = findViewById(R.id.calendarID);
        recyclerView = findViewById(R.id.respRV);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;

                String date = day + "/" + month + "/" + year;
                /*Toast.makeText(getApplicationContext(), "Wybrana data: " + date,
                        Toast.LENGTH_SHORT).show();*/
                responsibilitiesList = db.profileDao().getResponsibilityWithDate(date);
                adapter = new ResponsibilitiesRVAdapter(responsibilitiesList);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}