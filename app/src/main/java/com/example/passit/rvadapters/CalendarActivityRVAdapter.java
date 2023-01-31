package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.db.AppDatabase;
import com.example.passit.helpers.NotificationSender;
import com.example.passit.R;
import com.example.passit.ResponsibilityInfo;
import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.notificationbrodcasts.ReminderBroadcast;

import java.util.List;

public class CalendarActivityRVAdapter extends RecyclerView.Adapter<CalendarActivityRVAdapter.ViewHolder> {

    private final List<Responsibility> responsibilitiesList;
    private List<Notification> reminderNotification;
    private List<Notification> delayNotification;
    private AppDatabase db;
    private NotificationSender notificationSender;
    private NotificationManager notificationManager;
    private AlarmManager alarmManager;
    private int reminderId, delayId;

    public CalendarActivityRVAdapter(List<Responsibility> responsibilitiesList) {
        this.responsibilitiesList = responsibilitiesList;
    }

    @NonNull
    @Override
    public CalendarActivityRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_task_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CalendarActivityRVAdapter.ViewHolder holder, int position) {
        db = AppDatabase.getDbInstance(holder.respName.getContext());
        notificationSender = new NotificationSender(holder.respName.getContext());
        int pos = holder.getAdapterPosition();
        int respId = responsibilitiesList.get(pos).getResp_id();

        notificationManager = (NotificationManager) holder.respName.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) holder.respName.getContext().getSystemService(Context.ALARM_SERVICE);

        reminderId = db.profileDao().getNotificationId(respId, "Reminder");
        delayId = db.profileDao().getNotificationId(respId, "Delay");
        Intent intent = new Intent(holder.respName.getContext(), ReminderBroadcast.class);

        reminderNotification = db.profileDao().getNotificationById(reminderId);
        delayNotification = db.profileDao().getNotificationById(delayId);

        holder.respName.setText(responsibilitiesList.get(pos).getResp_name() + "\n"
                + db.profileDao().getSubjectName(responsibilitiesList.get(pos).getSubject_id()) +
                "\n\n\nTermin: " + responsibilitiesList.get(pos).getDate_due() + ", " + responsibilitiesList.get(pos).getHour_due());
        holder.respName.setBackgroundResource(R.color.cardBackground2);

        switch (responsibilitiesList.get(pos).getResponsibility_type()) {
            case "Task":
                if (responsibilitiesList.get(pos).isDelayed()) {
                    holder.progressTV.setText("Zaległe zadanie");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.highImportance));
                } else {
                    holder.progressTV.setText("Zadanie");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.white));
                }
                break;
            case "Test":
                if (responsibilitiesList.get(pos).isDelayed()) {
                    holder.progressTV.setText("Zaległe zaliczenie");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.highImportance));
                } else {
                    holder.progressTV.setText("Zaliczenie");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.white));
                }
                break;
        }

        if (db.profileDao().getResponsibilityState(respId)) {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
            holder.respName.setPaintFlags(holder.respName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.respName.setBackgroundResource(R.color.cardBackgroundFinished);
            holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.normalImportance));
        } else {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
            holder.respName.setPaintFlags(holder.respName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.respName.setBackgroundResource(R.color.cardBackground2);
            if (responsibilitiesList.get(pos).isDelayed()) {
                holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.highImportance));
            } else {
                holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.white));
            }

        }


        switch (responsibilitiesList.get(pos).getImportance()) {
            case "normal":
                holder.importanceLabel.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                holder.importanceLabel.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                holder.importanceLabel.setBackgroundResource(R.color.highImportance);
                break;
        }

        holder.markFinishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.profileDao().getResponsibilityState(respId)) {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
                    holder.respName.setPaintFlags(holder.respName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.respName.setBackgroundResource(R.color.cardBackground2);
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.white));
                    db.profileDao().setUnfinishedResponsibility(respId);

                    notificationSender.sendNotification(reminderNotification.get(0).getTime_to_trigger(), reminderNotification.get(0).getResp_id(), reminderNotification.get(0).getNotification_type());
                    notificationSender.sendNotification(delayNotification.get(0).getTime_to_trigger(), delayNotification.get(0).getResp_id(), delayNotification.get(0).getNotification_type());
                    db.profileDao().deleteNotificationById(reminderId);
                    db.profileDao().deleteNotificationById(delayId);
                    reminderId = db.profileDao().getNotificationId(respId, "Reminder");
                    delayId = db.profileDao().getNotificationId(respId, "Delay");
                } else {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
                    holder.respName.setPaintFlags(holder.respName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.respName.setBackgroundResource(R.color.cardBackgroundFinished);
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.normalImportance));
                    db.profileDao().setFinishedResponsibility(respId);

                    notificationSender.cancelNotification(reminderId);
                    notificationSender.cancelNotification(delayId);
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.respName.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Usuń zadanie");
                builder.setMessage("Czy na pewno chcesz usunąć " + responsibilitiesList.get(pos).getResp_name() + "?");
                builder.setCancelable(false);
                builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                    notificationSender.cancelNotification(reminderId);
                    notificationSender.cancelNotification(delayId);
                    db.profileDao().deleteNotificationById(reminderId);
                    db.profileDao().deleteNotificationById(delayId);
                    db.profileDao().deleteResponsibility(respId);
                    responsibilitiesList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, responsibilitiesList.size());
                    dialog.dismiss();
                });

                builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {

                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent2 = new Intent(view.getContext(), ResponsibilityInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("respId", respId);
            bundle.putString("calendar", "isCalendar");
            intent2.putExtras(bundle);
            view.getContext().startActivity(intent2);
        });
    }

    @Override
    public int getItemCount() {
        return responsibilitiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView respName, progressTV;
        private final Button markFinishedBtn, removeBtn;
        private final ImageView importanceLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            respName = itemView.findViewById(R.id.cv_noteInfo);
            markFinishedBtn = itemView.findViewById(R.id.markFinished);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
            progressTV = itemView.findViewById(R.id.progressTV);
        }
    }
}
