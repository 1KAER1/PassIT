package com.example.passit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passit.db.entities.Lesson;
import com.example.passit.db.entities.LessonDate;
import com.example.passit.db.entities.Subject;
import com.example.passit.rvadapters.ExerciseSummaryRVAdapter;
import com.example.passit.rvadapters.LabSummaryRVAdapter;
import com.example.passit.rvadapters.SummaryRVAdapter;

import java.util.ArrayList;
import java.util.Iterator;

public class SubjectInfoSummary extends Fragment {

    private RecyclerView lectureRV, exerciseRV, labRV;

    private TextView subjectName, ectsPoints;

    //Lecture texts
    private TextView lectureLecturerName, lectureFirstWeek, lectureSecondWeek,
            lectureLecturerTag, lectureWeeksTag, lectureDash, lectureTag;

    //Exercise texts
    private TextView exerciseLecturerName, exerciseFirstWeek, exerciseSecondWeek,
            exerciseLecturerTag, exerciseWeeksTag, exerciseDash, exerciseTag;

    //Lab texts
    private TextView labLecturerName, labFirstWeek, labSecondWeek,
            labLecturerTag, labWeeksTag, labDash, labTag;

    //lecture lists
    private ArrayList<String> dayListRV1 = new ArrayList<>();
    private ArrayList<String> hourListRV1 = new ArrayList<>();
    private ArrayList<Integer> periodListRV1 = new ArrayList<>();
    private ArrayList<String> lectureHourList = new ArrayList<>();
    private ArrayList<String> lectureDayList = new ArrayList<>();
    private ArrayList<Integer> lecturePeriodList = new ArrayList<>();

    //exercise lists
    private ArrayList<String> dayListRV2 = new ArrayList<>();
    private ArrayList<String> hourListRV2 = new ArrayList<>();
    private ArrayList<Integer> periodListRV2 = new ArrayList<>();
    private ArrayList<String> exerciseHourList = new ArrayList<>();
    private ArrayList<String> exerciseDayList = new ArrayList<>();
    private ArrayList<Integer> exercisePeriodList = new ArrayList<>();

    //lab lists
    private ArrayList<String> dayListRV3 = new ArrayList<>();
    private ArrayList<String> hourListRV3 = new ArrayList<>();
    private ArrayList<Integer> periodListRV3 = new ArrayList<>();
    private ArrayList<String> labHourList = new ArrayList<>();
    private ArrayList<String> labDayList = new ArrayList<>();
    private ArrayList<Integer> labPeriodList = new ArrayList<>();

    private SummaryRVAdapter lectureSummaryRVAdapter;
    private ExerciseSummaryRVAdapter exerciseSummaryRVAdapter;
    private LabSummaryRVAdapter labSummaryRVAdapter;

    private long pressedTime;
    private Button confirmButton;

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_info_summary, container, false);

        confirmButton = view.findViewById(R.id.confirmButton);

        subjectName = view.findViewById(R.id.subjectNameTV);
        ectsPoints = view.findViewById(R.id.ectsTV);

        //Lecture setup
        lectureLecturerName = view.findViewById(R.id.lectureLecturerName);
        lectureFirstWeek = view.findViewById(R.id.lectureFirstWeek);
        lectureSecondWeek = view.findViewById(R.id.lectureSecondWeek);
        lectureRV = view.findViewById(R.id.lectureRV);

        lectureLecturerTag = view.findViewById(R.id.lectureLecturerTag);
        lectureWeeksTag = view.findViewById(R.id.lectureWeeksTag);
        lectureDash = view.findViewById(R.id.lectureDash);
        lectureTag = view.findViewById(R.id.lectureTag);

        //Exercise setup
        exerciseLecturerName = view.findViewById(R.id.exerciseLecturerName);
        exerciseFirstWeek = view.findViewById(R.id.exerciseFirstWeek);
        exerciseSecondWeek = view.findViewById(R.id.exerciseSecondWeek);
        exerciseRV = view.findViewById(R.id.exerciseRV);

        exerciseLecturerTag = view.findViewById(R.id.exerciseLecturerTag);
        exerciseWeeksTag = view.findViewById(R.id.exerciseWeeksTag);
        exerciseDash = view.findViewById(R.id.exerciseDash);
        exerciseTag = view.findViewById(R.id.exerciseTag);

        //lab setup
        labLecturerName = view.findViewById(R.id.labLecturerName);
        labFirstWeek = view.findViewById(R.id.labFirstWeek);
        labSecondWeek = view.findViewById(R.id.labSecondWeek);
        labRV = view.findViewById(R.id.labRV);

        labLecturerTag = view.findViewById(R.id.labLecturerTag);
        labWeeksTag = view.findViewById(R.id.labWeeksTag);
        labDash = view.findViewById(R.id.labDash);
        labTag = view.findViewById(R.id.labTag);

        String lecturerTag = "Imię i nazwisko prowadzącego";
        String weeksTag = "Tygodnie, w których odbywają się zajęcia";

        getParentFragmentManager().setFragmentResultListener("summary", this, ((requestKeyB, resultB) -> {

            subjectName.setText(resultB.getString("subjectName"));
            ectsPoints.setText(String.valueOf(resultB.getInt("ectsPoints")));

            if (resultB.getBoolean("isLecture")) {
                getParentFragmentManager().setFragmentResultListener("lectureSummary", this, (((requestKey, result) -> {

                    int fWeek = result.getInt("firstWeek");
                    int lWeek = result.getInt("lastWeek");

                    lectureLecturerName.setText(result.getString("lecturerName"));
                    lectureFirstWeek.setText(String.valueOf(fWeek));
                    lectureSecondWeek.setText(String.valueOf(lWeek));
                    lectureDayList = result.getStringArrayList("dayList");
                    lectureHourList = result.getStringArrayList("hourList");
                    lecturePeriodList = result.getIntegerArrayList("lessonPeriodList");

                    lectureLecturerTag.setText(lecturerTag);
                    lectureWeeksTag.setText(weeksTag);
                    lectureDash.setText("-");
                    lectureTag.setText("wykład");

                    lectureSummaryRVAdapter = new SummaryRVAdapter(dayListRV1, hourListRV1, periodListRV1);
                    lectureRV.setAdapter(lectureSummaryRVAdapter);

                    Iterator<String> dayIterator = lectureDayList.iterator();
                    Iterator<String> hourIterator = lectureHourList.iterator();
                    Iterator<Integer> periodIterator = lecturePeriodList.iterator();

                    while (dayIterator.hasNext()) {
                        addItemToLectureRV(dayIterator.next(), hourIterator.next(), periodIterator.next());
                    }

                })));
            } else {
                lectureTag.setPaintFlags(lectureTag.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (resultB.getBoolean("isExercise")) {
                getParentFragmentManager().setFragmentResultListener("exerciseSummary", this, (((requestKey, result) -> {

                    int fWeek = result.getInt("firstWeek");
                    int lWeek = result.getInt("lastWeek");

                    exerciseLecturerName.setText(result.getString("lecturerName"));
                    exerciseFirstWeek.setText(String.valueOf(fWeek));
                    exerciseSecondWeek.setText(String.valueOf(lWeek));
                    exerciseDayList = result.getStringArrayList("dayList");
                    exerciseHourList = result.getStringArrayList("hourList");
                    exercisePeriodList = result.getIntegerArrayList("lessonPeriodList");

                    exerciseSummaryRVAdapter = new ExerciseSummaryRVAdapter(dayListRV2, hourListRV2, periodListRV2);
                    exerciseRV.setAdapter(exerciseSummaryRVAdapter);

                    Iterator<String> dayIterator = exerciseDayList.iterator();
                    Iterator<String> hourIterator = exerciseHourList.iterator();
                    Iterator<Integer> periodIterator = exercisePeriodList.iterator();

                    while (dayIterator.hasNext()) {
                        addItemToExerciseRV(dayIterator.next(), hourIterator.next(), periodIterator.next());
                    }

                })));
            } else {
                exerciseTag.setPaintFlags(exerciseTag.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (resultB.getBoolean("isLab")) {
                getParentFragmentManager().setFragmentResultListener("labSummary", this, (((requestKey, result) -> {

                    int fWeek = result.getInt("firstWeek");
                    int lWeek = result.getInt("lastWeek");

                    labLecturerName.setText(result.getString("lecturerName"));
                    labFirstWeek.setText(String.valueOf(fWeek));
                    labSecondWeek.setText(String.valueOf(lWeek));
                    labDayList = result.getStringArrayList("dayList");
                    labHourList = result.getStringArrayList("hourList");
                    labPeriodList = result.getIntegerArrayList("lessonPeriodList");

                    labSummaryRVAdapter = new LabSummaryRVAdapter(dayListRV3, hourListRV3, periodListRV3);
                    labRV.setAdapter(labSummaryRVAdapter);

                    Iterator<String> dayIterator = labDayList.iterator();
                    Iterator<String> hourIterator = labHourList.iterator();
                    Iterator<Integer> periodIterator = labPeriodList.iterator();

                    while (dayIterator.hasNext()) {
                        addItemToLabRV(dayIterator.next(), hourIterator.next(), periodIterator.next());
                    }

                })));
            } else {
                labTag.setPaintFlags(labTag.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResultListener("database", getViewLifecycleOwner(), ((requestKey, result1) -> {
                    AppDatabase db = AppDatabase.getDbInstance(getActivity());

                    Subject subject = new Subject();
                    subject.subject_name = result1.getString("subjectName");
                    subject.is_lecture = result1.getBoolean("isLecture");
                    subject.is_exercise = result1.getBoolean("isExercise");
                    subject.is_lab = result1.getBoolean("isLab");
                    subject.ects_points = result1.getInt("ectsPoints");
                    subject.importance = result1.getString("selectedImportance");
                    subject.profile_id = db.profileDao().getActiveProfile();
                    db.profileDao().insertSubject(subject);

                    if (result1.getBoolean("isLecture")) {
                        getParentFragmentManager().setFragmentResultListener("lectureDatabase", getViewLifecycleOwner(), ((requestKey1, result) -> {
                            String lessonType = "Lecture";

                            Lesson lecture = new Lesson();
                            lecture.lesson_type = lessonType;
                            lecture.lecturer_name = result.getString("lecturerName");
                            lecture.starting_week = result.getInt("firstWeek");
                            lecture.ending_week = result.getInt("lastWeek");

                            ArrayList<String> lectureDays = result.getStringArrayList("dayList");
                            ArrayList<String> lectureHours = result.getStringArrayList("hourList");
                            ArrayList<Integer> lecturePeriod = result.getIntegerArrayList("lessonPeriodList");

                            int subjectID = db.profileDao().getSubjectId(result1.getString("subjectName"));
                            lecture.subject_id = subjectID;

                            db.profileDao().insertLesson(lecture);

                            LessonDate lessonDate = new LessonDate();
                            Iterator<String> dayIterator = lectureDays.iterator();
                            Iterator<String> hourIterator = lectureHours.iterator();
                            Iterator<Integer> periodIterator = lecturePeriod.iterator();

                            while (dayIterator.hasNext()) {
                                lessonDate.day_name = dayIterator.next();
                                lessonDate.lesson_time = hourIterator.next();
                                lessonDate.lesson_id = db.profileDao().getLessonId(lessonType, subjectID);
                                lessonDate.lesson_period = periodIterator.next();
                                db.profileDao().insertLessonDate(lessonDate);
                            }
                        }));
                    }

                    if (result1.getBoolean("isExercise")) {
                        getParentFragmentManager().setFragmentResultListener("exerciseDatabase", getViewLifecycleOwner(), ((requestKey1, result) -> {
                            String lessonType = "Exercise";

                            Lesson exercise = new Lesson();
                            exercise.lesson_type = lessonType;
                            exercise.lecturer_name = result.getString("lecturerName");
                            exercise.starting_week = result.getInt("firstWeek");
                            exercise.ending_week = result.getInt("lastWeek");

                            ArrayList<String> exerciseDays = result.getStringArrayList("dayList");
                            ArrayList<String> exerciseHours = result.getStringArrayList("hourList");
                            ArrayList<Integer> exercisePeriod = result.getIntegerArrayList("lessonPeriodList");

                            int subjectID = db.profileDao().getSubjectId(result1.getString("subjectName"));
                            exercise.subject_id = subjectID;

                            db.profileDao().insertLesson(exercise);

                            LessonDate lessonDate = new LessonDate();
                            Iterator<String> dayIterator = exerciseDays.iterator();
                            Iterator<String> hourIterator = exerciseHours.iterator();
                            Iterator<Integer> periodIterator = exercisePeriod.iterator();

                            while (dayIterator.hasNext()) {
                                lessonDate.day_name = dayIterator.next();
                                lessonDate.lesson_time = hourIterator.next();
                                lessonDate.lesson_id = db.profileDao().getLessonId(lessonType, subjectID);
                                lessonDate.lesson_period = periodIterator.next();
                                db.profileDao().insertLessonDate(lessonDate);
                            }
                        }));
                    }

                    if (result1.getBoolean("isLab")) {
                        getParentFragmentManager().setFragmentResultListener("labDatabase", getViewLifecycleOwner(), ((requestKey1, result) -> {
                            String lessonType = "Lab";

                            Lesson lab = new Lesson();
                            lab.lesson_type = lessonType;
                            lab.lecturer_name = result.getString("lecturerName");
                            lab.starting_week = result.getInt("firstWeek");
                            lab.ending_week = result.getInt("lastWeek");

                            ArrayList<String> labDays = result.getStringArrayList("dayList");
                            ArrayList<String> labHours = result.getStringArrayList("hourList");
                            ArrayList<Integer> labPeriod = result.getIntegerArrayList("lessonPeriodList");

                            int subjectID = db.profileDao().getSubjectId(result1.getString("subjectName"));
                            lab.subject_id = subjectID;

                            db.profileDao().insertLesson(lab);

                            LessonDate lessonDate = new LessonDate();
                            Iterator<String> dayIterator = labDays.iterator();
                            Iterator<String> hourIterator = labHours.iterator();
                            Iterator<Integer> periodIterator = labPeriod.iterator();

                            while (dayIterator.hasNext()) {
                                lessonDate.day_name = dayIterator.next();
                                lessonDate.lesson_time = hourIterator.next();
                                lessonDate.lesson_id = db.profileDao().getLessonId(lessonType, subjectID);
                                lessonDate.lesson_period = periodIterator.next();
                                db.profileDao().insertLessonDate(lessonDate);
                            }
                        }));
                    }

                }));

                Intent intent = new Intent(getActivity(), SubjectsView.class);
                startActivity(intent);

            }
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addItemToLectureRV(String day, String hour, int period) {
        if (!day.isEmpty()) {
            dayListRV1.add(day);
            hourListRV1.add(hour);
            periodListRV1.add(period);
            lectureSummaryRVAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addItemToExerciseRV(String day, String hour, int period) {
        if (!day.isEmpty()) {
            dayListRV2.add(day);
            hourListRV2.add(hour);
            periodListRV2.add(period);
            exerciseSummaryRVAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addItemToLabRV(String day, String hour, int period) {
        if (!day.isEmpty()) {
            dayListRV3.add(day);
            hourListRV3.add(hour);
            periodListRV3.add(period);
            labSummaryRVAdapter.notifyDataSetChanged();
        }
    }
}