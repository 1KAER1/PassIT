package com.example.passit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ExerciseInfo extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView classTimeRV;
    private ClassTimeRVAdapter classTimeRVAdapter;
    private Spinner dayPicker;
    private Button timeButton, nextButton;
    private int hour, minute;
    private ArrayList<String> dayList = new ArrayList<>();
    private ArrayList<String> hourList = new ArrayList<>();
    private Button addDayButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exercise_info, container, false);


        classTimeRV = view.findViewById(R.id.classesTimeRV);
        timeButton = view.findViewById(R.id.timeButton);
        addDayButton = view.findViewById(R.id.addDayBtn);
        nextButton = view.findViewById(R.id.nextBtn);

        dayPicker = view.findViewById(R.id.dayPicker);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.days, R.layout.custom_spinner_layout);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_spinner_layout);
        dayPicker.setAdapter(adapter);
        dayPicker.setOnItemSelectedListener(this);

        classTimeRVAdapter = new ClassTimeRVAdapter(dayList, hourList);
        classTimeRV.setAdapter(classTimeRVAdapter);

        timeButton.setOnClickListener(view1 -> popTimePicker());


        addDayButton.setOnClickListener(view2 -> {
            String pickedDay = dayPicker.getSelectedItem().toString();
            String pickedTime = timeButton.getText().toString();

            if (TextUtils.isEmpty(pickedTime)) {
                Toast.makeText(getActivity(), "Wybierz godzinę!",
                        Toast.LENGTH_LONG).show();
            } else {
                if (classTimeRVAdapter.getItemCount() < 6) {
                    Toast.makeText(getActivity(), "Number of items in RV: " + classTimeRVAdapter.getItemCount(),
                            Toast.LENGTH_SHORT).show();
                    addItem(pickedDay, pickedTime);
                } else {
                    Toast.makeText(getActivity(), "Dodano już maksymalną liczbę dat!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResultListener("exercise", getViewLifecycleOwner(), ((requestKey, result) -> {
                    if (!result.getBoolean("isLab")) {
                        Navigation.findNavController(view).navigate(R.id.action_exerciseInfo_to_subjectInfoSummary);
                    } else if (result.getBoolean("isLab")) {
                        Navigation.findNavController(view).navigate(R.id.navigateToLab);
                    }
                }));
            }
        });

        //TO REMOVE
        //nextButton.setOnClickListener(view12 -> Navigation.findNavController(view12).navigate(R.id.navigateToLab));

        return view;
    }

    public void popTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);

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