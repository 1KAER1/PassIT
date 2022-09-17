package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.rvadapters.ResponsibilitiesRVAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageButton addSubjectButton, respButton, notesButton, calendarButton;
    private RecyclerView importantRespRV;
    private ResponsibilitiesRVAdapter adapter;
    private long pressedTime;
    private AppDatabase db;
    private List<Profile> profilesList = new ArrayList<>();
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<String> responsibilitiesDates = new ArrayList<>();
    private List<Responsibility> responsibilitiesRV = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addSubjectButton = findViewById(R.id.subjectsBtn);
        respButton = findViewById(R.id.respButton);
        notesButton = findViewById(R.id.notesViewBtn);
        calendarButton = findViewById(R.id.calendarViewBtn);
        importantRespRV = findViewById(R.id.importantResp);

        db = AppDatabase.getDbInstance(this);

        profilesList = db.profileDao().getAllProfiles();
        responsibilitiesDates = db.profileDao().getResponsibilitiesDates();
        responsibilitiesList = db.profileDao().getAllResponsibilities();
        if (responsibilitiesList != null) {
            checkResponsibilitiesDelay();
        }

        sortRespDates();

        if (profilesList.isEmpty()) {
            addNewProfile();
        }

        addSubjectButton.setOnClickListener(view -> openView(SubjectsView.class));
        respButton.setOnClickListener(view -> openView(ResponsibilitiesView.class));
        notesButton.setOnClickListener(view -> openView(NotesView.class));
        calendarButton.setOnClickListener(view -> openView(CalendarActivity.class));

    }

    public void sortRespDates() {
        responsibilitiesDates.sort(new Comparator<String>() {
            @SuppressLint("SimpleDateFormat")
            final
            DateFormat format = new SimpleDateFormat("d/MM/yyyy");

            @Override
            public int compare(String o1, String o2) {
                try {
                    return Objects.requireNonNull(format.parse(o1)).compareTo(format.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
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

    public void addNewProfile() {
        Intent intent = new Intent(this, AddNewProfile.class);
        startActivity(intent);
    }

    public void openView(Class<?> viewClass) {
        Intent intent = new Intent(this, viewClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            this.finishAffinity();
        } else {
            Toast.makeText(this, "Kliknij jeszcze raz, aby zamknąć", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}