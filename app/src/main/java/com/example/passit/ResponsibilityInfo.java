package com.example.passit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Responsibility;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ResponsibilityInfo extends AppCompatActivity {

    private int respId;
    private boolean fromCalendar, fromMenu = false;
    private MaterialAutoCompleteTextView respNameTV, respTypeTV, dateTV, respDescriptionTV, assignedSubjectTV;
    private TextInputLayout dateDueTIL;
    private AppDatabase db;
    private NotificationSender notificationSender;
    private ImageView importanceCircle;
    private List<Responsibility> responsibilitiesList = new ArrayList<>();
    private List<Notification> reminderNotification = new ArrayList<>();
    private List<Notification> delayNotification = new ArrayList<>();
    private Button deleteBtn, editBtn, finishBtn, returnBtn;
    private boolean editedDate = false;
    private int reminderId, delayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsibility_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            respId = extras.getInt("respId");
            if (extras.getString("calendar") != null) {
                fromCalendar = true;
            }
            if (extras.getString("menu") != null) {
                fromMenu = true;
            }
        }

        notificationSender = new NotificationSender(this);

        respNameTV = findViewById(R.id.respNameET);
        respTypeTV = findViewById(R.id.respTypeET);
        assignedSubjectTV = findViewById(R.id.assignedSubjectET);
        dateTV = findViewById(R.id.dateDueET);
        respDescriptionTV = findViewById(R.id.noOfSubjectsTV);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);
        finishBtn = findViewById(R.id.markFinishedBtn);
        returnBtn = findViewById(R.id.returnBtn);
        importanceCircle = findViewById(R.id.importanceCircle);
        dateDueTIL = findViewById(R.id.dateDueId);

        db = AppDatabase.getDbInstance(this);
        reminderId = db.profileDao().getNotificationId(respId, "Reminder");
        delayId = db.profileDao().getNotificationId(respId, "Delay");

        responsibilitiesList = db.profileDao().getResponsibilityWithId(respId);
        reminderNotification = db.profileDao().getNotificationById(reminderId);
        delayNotification = db.profileDao().getNotificationById(delayId);

        if (db.profileDao().getResponsibilityState(respId)) {
            finishBtn.setBackgroundResource(R.drawable.finish_button);
        } else {
            finishBtn.setBackgroundResource(R.drawable.unfinished_button);
        }

        if (responsibilitiesList.size() > 0) {
            fillInfo();
        }

        deleteBtn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            builder.setTitle("Usuń zadanie");
            builder.setMessage("Czy na pewno chcesz usunąć " + responsibilitiesList.get(0).getResp_name() + "?");
            builder.setCancelable(false);
            builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                notificationSender.cancelNotification(reminderId);
                notificationSender.cancelNotification(delayId);
                db.profileDao().deleteNotificationById(reminderId);
                db.profileDao().deleteNotificationById(delayId);
                db.profileDao().deleteResponsibility(respId);
                dialog.dismiss();
                returnToView();
            });

            builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        finishBtn.setOnClickListener(view -> {

            if (db.profileDao().getResponsibilityState(respId)) {
                finishBtn.setBackgroundResource(R.drawable.unfinished_button);
                db.profileDao().setUnfinishedResponsibility(respId);
                notificationSender.sendNotification(reminderNotification.get(0).getTime_to_trigger(), reminderNotification.get(0).getResp_id(), reminderNotification.get(0).getNotification_type());
                notificationSender.sendNotification(delayNotification.get(0).getTime_to_trigger(), delayNotification.get(0).getResp_id(), delayNotification.get(0).getNotification_type());
                db.profileDao().deleteNotificationById(reminderId);
                db.profileDao().deleteNotificationById(delayId);
                reminderId = db.profileDao().getNotificationId(respId, "Reminder");
                delayId = db.profileDao().getNotificationId(respId, "Delay");
            } else {
                finishBtn.setBackgroundResource(R.drawable.finish_button);
                db.profileDao().setFinishedResponsibility(respId);
                notificationSender.cancelNotification(reminderId);
                notificationSender.cancelNotification(delayId);
            }
        });

        editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddResponsibility.class);
            Bundle bundle = new Bundle();
            bundle.putInt("respId", respId);
            if (fromCalendar) {
                bundle.putString("calendar", "isCalendar");
            } else if (fromMenu) {
                bundle.putString("menu", "isMenu");
            }
            intent.putExtras(bundle);
            startActivity(intent);
        });

        returnBtn.setOnClickListener(view -> {
            if (fromCalendar) {
                returnToCalendar();
            } else if (fromMenu) {
                returnToMenu();
            } else {
                returnToView();
            }
        });

    }

    public void fillInfo() {
        String dateWithHour = responsibilitiesList.get(0).getDate_due() + "  " + responsibilitiesList.get(0).getHour_due();
        String assignedSubject = db.profileDao().getSubjectName(responsibilitiesList.get(0).getSubject_id()) + "\n("
                + responsibilitiesList.get(0).getSubject_type() + ")";

        respNameTV.setText(responsibilitiesList.get(0).getResp_name());
        switch (responsibilitiesList.get(0).getResponsibility_type()) {
            case "Task":
                respTypeTV.setText("Zadanie");
                dateDueTIL.setHint("Termin oddania zadania");
                break;
            case "Test":
                respTypeTV.setText("Zaliczenie");
                dateDueTIL.setHint("Data zaliczenia");
                break;
        }

        switch (responsibilitiesList.get(0).getImportance()) {
            case "normal":
                importanceCircle.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                importanceCircle.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                importanceCircle.setBackgroundResource(R.color.highImportance);
                break;
        }
        assignedSubjectTV.setText(assignedSubject);
        dateTV.setText(dateWithHour);
        respDescriptionTV.setText(responsibilitiesList.get(0).getDescription());
    }

    @Override
    public void onBackPressed() {
        if (fromCalendar) {
            returnToCalendar();
        } else if (fromMenu) {
            returnToMenu();
        } else {
            returnToView();
        }
    }

    public void returnToView() {
        Intent intent = new Intent(this, ResponsibilitiesView.class);
        startActivity(intent);
    }

    public void returnToCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("editedDate", responsibilitiesList.get(0).getDate_due());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void returnToMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}