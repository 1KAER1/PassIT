package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.NotificationSender;
import com.example.passit.R;
import com.example.passit.ResponsibilityInfo;
import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Responsibility;

import java.util.List;

public class ResponsibilitiesRVAdapter extends RecyclerView.Adapter<ResponsibilitiesRVAdapter.ViewHolder> {

    private final List<Responsibility> responsibilitiesList;
    private List<Notification> reminderNotification;
    private List<Notification> delayNotification;
    private AppDatabase db;
    private NotificationSender notificationSender;
    private NotificationManager notificationManager;
    private int reminderId, delayId;

    public ResponsibilitiesRVAdapter(List<Responsibility> responsibilitiesList) {
        this.responsibilitiesList = responsibilitiesList;
    }

    @NonNull
    @Override
    public ResponsibilitiesRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_task_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResponsibilitiesRVAdapter.ViewHolder holder, int position) {
        db = AppDatabase.getDbInstance(holder.respName.getContext());
        notificationSender = new NotificationSender(holder.respName.getContext());
        int pos = holder.getAdapterPosition();
        int respId = responsibilitiesList.get(pos).getResp_id();
        notificationManager = (NotificationManager) holder.respName.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        reminderId = db.profileDao().getNotificationId(respId, "Reminder");
        delayId = db.profileDao().getNotificationId(respId, "Delay");
        Log.d("NOT", "NOTIFICATION IDS: " + reminderId + "     DELAY: " + delayId);
        reminderNotification = db.profileDao().getNotificationById(reminderId);
        delayNotification = db.profileDao().getNotificationById(delayId);

        holder.respName.setText(responsibilitiesList.get(pos).getResp_name() + "\n"
                + db.profileDao().getSubjectName(responsibilitiesList.get(pos).getSubject_id()) +
                "\n\n\nTermin: " + responsibilitiesList.get(pos).getDate_due());
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
                    if (responsibilitiesList.get(pos).isDelayed()) {
                        holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.highImportance));
                    } else {
                        holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.white));
                    }
                    db.profileDao().setUnfinishedResponsibility(respId);
                    //TODO ADD NOTIFICATIONS

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
                    //TODO CANCEL NOTIFICATIONS
                    Log.d("NOT1", "NOTIFICATION IDS: " + reminderId + "     DELAY: " + delayId);
                    notificationManager.cancel(reminderId);
                    notificationManager.cancel(delayId);
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationManager.cancel(reminderId);
                notificationManager.cancel(delayId);
                db.profileDao().deleteNotificationById(reminderId);
                db.profileDao().deleteNotificationById(delayId);
                db.profileDao().deleteResponsibility(respId);
                responsibilitiesList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, responsibilitiesList.size());
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ResponsibilityInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("respId", respId);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return responsibilitiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView respName, progressTV;
        private final ConstraintLayout rowLayout;
        private final Button markFinishedBtn, removeBtn;
        private final ImageView importanceLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            respName = itemView.findViewById(R.id.cv_noteInfo);
            markFinishedBtn = itemView.findViewById(R.id.markFinished);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            rowLayout = itemView.findViewById(R.id.rowLayout);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
            progressTV = itemView.findViewById(R.id.progressTV);
        }
    }
}
