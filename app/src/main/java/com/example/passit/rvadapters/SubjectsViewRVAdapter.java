package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.NotificationSender;
import com.example.passit.R;
import com.example.passit.SubjectDetails;
import com.example.passit.db.entities.Notification;
import com.example.passit.db.entities.Responsibility;
import com.example.passit.db.entities.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsViewRVAdapter extends RecyclerView.Adapter<SubjectsViewRVAdapter.ViewHolder> implements Filterable {

    private final List<Subject> subjectNameList1;
    private final List<Subject> subjectNameListFull;
    private AppDatabase db;

    public SubjectsViewRVAdapter(List<Subject> subjectNameList1) {
        this.subjectNameList1 = subjectNameList1;
        subjectNameListFull = new ArrayList<>(subjectNameList1);
    }

    @NonNull
    @Override
    public SubjectsViewRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_subject_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SubjectsViewRVAdapter.ViewHolder holder, int position) {

        db = AppDatabase.getDbInstance(holder.subjectName.getContext());
        int subjectId = subjectNameList1.get(holder.getAdapterPosition()).getSubject_id();

        holder.subjectName.setText(subjectNameList1.get(holder.getAdapterPosition()).getSubject_name());
        holder.subjectName.setBackgroundResource(R.color.cardBackground);
        holder.progressTV.setText("W trakcie");
        holder.progressTV.setTextColor(ContextCompat.getColor(holder.subjectName.getContext(), R.color.white));

        if (db.profileDao().getSubjectState(subjectId)) {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
            holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.subjectName.setBackgroundResource(R.color.cardBackgroundFinished);
            holder.progressTV.setText("Zaliczony");
            holder.progressTV.setTextColor(ContextCompat.getColor(holder.subjectName.getContext(), R.color.normalImportance));
        } else {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
            holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.subjectName.setBackgroundResource(R.color.cardBackground);
            holder.progressTV.setText("W trakcie");
            holder.progressTV.setTextColor(ContextCompat.getColor(holder.subjectName.getContext(), R.color.white));
        }

        switch (subjectNameList1.get(holder.getAdapterPosition()).getImportance()) {
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
                if (db.profileDao().getSubjectState(subjectId)) {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
                    holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.subjectName.setBackgroundResource(R.color.cardBackground);
                    holder.progressTV.setText("W trakcie");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.subjectName.getContext(), R.color.white));
                    db.profileDao().setSubjectInProgress(subjectId);
                } else {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
                    holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.subjectName.setBackgroundResource(R.color.cardBackgroundFinished);
                    holder.progressTV.setText("Zaliczony");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.subjectName.getContext(), R.color.normalImportance));
                    db.profileDao().setPassedSubject(subjectId);
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.subjectName.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Usuń przedmiot");
                builder.setMessage("Czy na pewno chcesz usunąć przedmiot " + subjectNameList1.get(holder.getAdapterPosition()).getSubject_name() + "? Usunięcie przedmiotu usunie wszystkie przypisane do niego zadania!");
                builder.setCancelable(false);
                builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                    NotificationSender notificationSender = new NotificationSender(holder.subjectName.getContext());
                    List<Responsibility> responsibilities = db.profileDao().getResponsibilityWithSubjectId(subjectNameList1.get(holder.getAdapterPosition()).subject_id);

                    for (Responsibility resp : responsibilities) {
                        notificationSender.cancelNotification(db.profileDao().getNotificationId(resp.getResp_id(), "Reminder"));
                        notificationSender.cancelNotification(db.profileDao().getNotificationId(resp.getResp_id(), "Delay"));
                        db.profileDao().deleteNotificationById(db.profileDao().getNotificationId(resp.getResp_id(), "Reminder"));
                        db.profileDao().deleteNotificationById(db.profileDao().getNotificationId(resp.getResp_id(), "Delay"));
                        db.profileDao().deleteResponsibility(resp.getResp_id());
                    }

                    db.profileDao().deleteSubject(subjectId);
                    subjectNameList1.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), subjectNameList1.size());
                    dialog.dismiss();
                });

                builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SubjectDetails.class);
                Bundle bundle = new Bundle();
                bundle.putInt("subjectId", subjectId);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectNameList1.size();
    }

    @Override
    public Filter getFilter() {
        return respFilter;
    }

    private Filter respFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Subject> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(subjectNameListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Subject subject : subjectNameListFull) {
                    if (subject.getSubject_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(subject);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            subjectNameList1.clear();
            subjectNameList1.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectName, progressTV;
        private final ConstraintLayout rowLayout;
        private final Button markFinishedBtn, removeBtn;
        private final ImageView importanceLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.cv_noteInfo);
            markFinishedBtn = itemView.findViewById(R.id.markFinished);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            rowLayout = itemView.findViewById(R.id.rowLayout);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
            progressTV = itemView.findViewById(R.id.progressTV);
        }
    }
}
