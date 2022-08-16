package com.example.passit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.passit.rvadapters.ClassTimeRVAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class LectureInfo extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView classTimeRV;
    private ClassTimeRVAdapter classTimeRVAdapter;
    private Spinner dayPicker;
    private Button timeButton, nextButton, addDayButton;
    private EditText lecturerName, firstWeek, lastWeek;
    private int hour, minute;
    private ArrayList<String> dayList = new ArrayList<>();
    private ArrayList<String> hourList = new ArrayList<>();
    private long pressedTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {

                    //TODO Change MainActivity to Subject Activity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Kliknij jeszcze raz, aby opuścić. Dane nie zostaną zapisane.", Toast.LENGTH_SHORT).show();
                }
                pressedTime = System.currentTimeMillis();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lecture_info, container, false);


        classTimeRV = view.findViewById(R.id.classesTimeRV);
        timeButton = view.findViewById(R.id.timeButton);
        addDayButton = view.findViewById(R.id.addDayBtn);
        nextButton = view.findViewById(R.id.nextBtn);
        lecturerName = view.findViewById(R.id.lecturerName);
        firstWeek = view.findViewById(R.id.firstWeeks);
        lastWeek = view.findViewById(R.id.secondWeeks);

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

                AppDatabase db = AppDatabase.getDbInstance(getActivity());

                /*Profile profile = new Profile();
                profile.profile_name = "Biotechnologia";
                profile.semester = 7;
                db.profileDao().insertProfile(profile);*/

                if (checkInput()) {
                    passBundleInformation();
                    getParentFragmentManager().setFragmentResultListener("lecture", getViewLifecycleOwner(), ((requestKey, result) -> {
                        if (!result.getBoolean("isExercise") && !result.getBoolean("isLab")) {
                            Navigation.findNavController(view).navigate(R.id.action_lectureInfo_to_subjectInfoSummary);
                        } else if (result.getBoolean("isExercise")) {
                            Navigation.findNavController(view).navigate(R.id.navigateToExercise);
                        } else if (!result.getBoolean("isExercise") && result.getBoolean("isLab")) {
                            Navigation.findNavController(view).navigate(R.id.action_lectureInfo_to_labInfo);
                        }
                    }));
                } else {
                    Toast.makeText(getActivity(), "Uzupełnij wszystkie dane!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public boolean checkInput() {

        return !TextUtils.isEmpty(lecturerName.getText())
                && !TextUtils.isEmpty(firstWeek.getText())
                && !TextUtils.isEmpty(lastWeek.getText())
                && !dayList.isEmpty()
                && !hourList.isEmpty();
    }

    public void passBundleInformation(){
        Bundle bundle = new Bundle();
        bundle.putString("lecturerName", lecturerName.getText().toString());
        bundle.putInt("firstWeek", Integer.parseInt(String.valueOf(firstWeek.getText())));
        bundle.putInt("lastWeek", Integer.parseInt(String.valueOf(lastWeek.getText())));
        bundle.putStringArrayList("dayList", dayList);
        bundle.putStringArrayList("hourList", hourList);

        getParentFragmentManager().setFragmentResult("lectureSummary", bundle);
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