package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class AddClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView classTimeRV;
    private ClassTimeRVAdapter classTimeRVAdapter;
    private Spinner dayPicker;
    private Button timeButton;
    private int hour, minute;
    private ArrayList<String> dayList = new ArrayList<>();
    private ArrayList<String> hourList = new ArrayList<>();
    private Button addDayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        classTimeRV = findViewById(R.id.classesTimeRV);
        timeButton = findViewById(R.id.timeButton);
        addDayButton = findViewById(R.id.addDayBtn);

        dayPicker = findViewById(R.id.dayPicker);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, R.layout.custom_spinner_layout);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
        dayPicker.setAdapter(adapter);
        dayPicker.setOnItemSelectedListener(this);

        classTimeRVAdapter = new ClassTimeRVAdapter(dayList, hourList);
        classTimeRV.setAdapter(classTimeRVAdapter);

        addDayButton.setOnClickListener(view -> {
            String pickedDay = dayPicker.getSelectedItem().toString();
            String pickedTime = timeButton.getText().toString();

            if (TextUtils.isEmpty(pickedTime)) {
                Toast.makeText(getApplicationContext(), "Wybierz godzinę!",
                        Toast.LENGTH_LONG).show();
            } else {
                if (classTimeRVAdapter.getItemCount() < 6) {
                    Toast.makeText(getApplicationContext(), "Number of items in RV: " + classTimeRVAdapter.getItemCount(),
                            Toast.LENGTH_SHORT).show();
                    addItem(pickedDay, pickedTime);
                } else {
                    Toast.makeText(getApplicationContext(), "Dodano już maksymalną liczbę dat!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addItem(String day, String hour) {
        if (!day.isEmpty()) {
            dayList.add(day);
            hourList.add(hour);
            classTimeRVAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}