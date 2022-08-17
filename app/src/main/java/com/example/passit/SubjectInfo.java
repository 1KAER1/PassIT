package com.example.passit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.passit.db.entities.Profile;
import com.example.passit.db.entities.Subject;


public class SubjectInfo extends Fragment {

    private Button nextButton;
    private EditText subjectName;
    private EditText ectsPoints;
    private CheckBox lectureCB, exerciseCB, labCB;
    private RadioButton normalImportance, mediumImportance, highImportance;
    private RadioGroup importanceRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_info, container, false);

        nextButton = view.findViewById(R.id.nextBtn);
        subjectName = view.findViewById(R.id.lecturerName);
        ectsPoints = view.findViewById(R.id.ectsPoints);
        lectureCB = view.findViewById(R.id.lectureCB);
        exerciseCB = view.findViewById(R.id.exerciseCB);
        labCB = view.findViewById(R.id.labCB);
        normalImportance = view.findViewById(R.id.normalImportance);
        mediumImportance = view.findViewById(R.id.mediumImportance);
        highImportance = view.findViewById(R.id.highImportance);
        importanceRadioGroup = view.findViewById(R.id.importanceRadioGroup);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppDatabase db = AppDatabase.getDbInstance(getActivity());

                /*Profile profile = new Profile();
                profile.profile_name = "Informatyka";
                profile.semester = 7;
                db.profileDao().insertProfile(profile);*/

                /*Profile profile2 = new Profile();
                profile2.profile_name = "AiR";
                profile2.semester = 5;
                db.profileDao().insertProfile(profile2);*/

                db.profileDao().deactivateProfiles();
                db.profileDao().activateProfile("Informatyka");

                int activeProfileId = db.profileDao().getActiveProfile();
                Toast.makeText(getActivity(), "Aktywny profil: " + activeProfileId + " " + db.profileDao().getActiveProfileName(),
                        Toast.LENGTH_SHORT).show();

                String selectedImportance;
                selectedImportance = checkImportanceSelection();

                if (TextUtils.isEmpty(subjectName.getText()) || TextUtils.isEmpty(ectsPoints.getText()) || selectedImportance == null || !checkLessonType()) {
                    Toast.makeText(getActivity(), "Uzupełnij wszystkie dane!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    passBundleInformation(selectedImportance);

                    if (lectureCB.isChecked()) {
                        Navigation.findNavController(view).navigate(R.id.navigateToLecture);
                    } else {
                        if (exerciseCB.isChecked()) {
                            Navigation.findNavController(view).navigate(R.id.action_subjectInfo_to_exerciseInfo);
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_subjectInfo_to_labInfo);
                        }
                    }

                }
            }
        });

        return view;
    }

    public boolean checkLessonType() {
        return lectureCB.isChecked() || exerciseCB.isChecked() || labCB.isChecked();
    }

    public void passBundleInformation(String importance) {

        Bundle subjectBundle = new Bundle();
        subjectBundle.putString("subjectName", subjectName.getText().toString());
        subjectBundle.putBoolean("isLecture", lectureCB.isChecked());
        subjectBundle.putBoolean("isExercise", exerciseCB.isChecked());
        subjectBundle.putBoolean("isLab", labCB.isChecked());
        subjectBundle.putInt("ectsPoints", Integer.parseInt(ectsPoints.getText().toString()));
        subjectBundle.putString("selectedImportance", importance);

        getParentFragmentManager().setFragmentResult("lecture", subjectBundle);
        getParentFragmentManager().setFragmentResult("exercise", subjectBundle);
        getParentFragmentManager().setFragmentResult("lab", subjectBundle);
        getParentFragmentManager().setFragmentResult("summary", subjectBundle);
        getParentFragmentManager().setFragmentResult("database", subjectBundle);

    }

    public String checkImportanceSelection() {

        String selectedImportance;
        if (normalImportance.isChecked()) {
            selectedImportance = "normal";
        } else if (mediumImportance.isChecked()) {
            selectedImportance = "medium";
        } else if (highImportance.isChecked()) {
            selectedImportance = "high";
        } else {
            selectedImportance = null;
        }
        return selectedImportance;
    }
}