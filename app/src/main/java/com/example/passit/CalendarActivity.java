package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.passit.db.AppDatabase;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.rvadapters.CalendarActivityRVAdapter;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<Responsibility> responsibilitiesList2 = new ArrayList<>();
    private CalendarActivityRVAdapter adapter;
    private String selectedDate;
    private boolean specificDate = false;
    List<Calendar> datesToHighlight = new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_activity);

        setTitle("Kalendarz");

        db = AppDatabase.getDbInstance(this);

        calendarView = (CalendarView) findViewById(R.id.calendarID);
        recyclerView = findViewById(R.id.respRV);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = extras.getString("editedDate");
            specificDate = true;
            try {
                Calendar calendar = Calendar.getInstance();
                Date date = sdf.parse(selectedDate);
                assert date != null;
                calendar.setTime(date);
                calendarView.setDate(calendar);
            } catch (ParseException | OutOfDateRangeException e) {
                e.printStackTrace();
            }
        }

        if (specificDate) {
            responsibilitiesList = db.profileDao().getResponsibilityWithDate(selectedDate);
        } else {
            responsibilitiesList = db.profileDao().getResponsibilityWithDate(getTodaysDate());
        }

        try {
            responsibilitiesList2 = db.profileDao().getAllResponsibilities();
            datesToHighlight = convertDatesToCalendar(responsibilitiesList2);
            calendarView.setHighlightedDays(datesToHighlight);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        adapter = new CalendarActivityRVAdapter(responsibilitiesList);
        recyclerView.setAdapter(adapter);

        calendarView.setOnDayClickListener(eventDay -> {
            long dateInMillis = eventDay.getCalendar().getTimeInMillis();
            Date sDate = new Date(dateInMillis);
            String date = sdf.format(sDate);
            responsibilitiesList = db.profileDao().getResponsibilityWithDate(date);
            adapter = new CalendarActivityRVAdapter(responsibilitiesList);
            recyclerView.setAdapter(adapter);
        });
    }

    private List<Calendar> convertDatesToCalendar(List<Responsibility> responsibilitiesList) throws ParseException {
        List<Calendar> calendars = new ArrayList<>();
        List<EventDay> events = new ArrayList<>();

        for (Responsibility resp : responsibilitiesList) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
            Date date1 = sdf.parse(resp.getDate_due());
            Calendar calendar = Calendar.getInstance();
            assert date1 != null;
            calendar.setTime(date1);
            calendars.add(calendar);

            events.add(new EventDay(calendar, R.drawable.ic_highlight));
        }

        calendarView.setEvents(events);
        return calendars;
    }

    public String getSelectedDate() {
        return selectedDate;
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