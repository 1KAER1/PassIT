package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.R;
import com.example.passit.ResponsibilityInfo;
import com.example.passit.TaskInfo;
import com.example.passit.db.entities.Responsibility;

import java.util.List;

public class ResponsibilitiesRVAdapter extends RecyclerView.Adapter<ResponsibilitiesRVAdapter.ViewHolder> {

    private final List<Responsibility> responsibilitiesList;
    private AppDatabase db;

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
        int pos = holder.getAdapterPosition();
        int respId = responsibilitiesList.get(pos).getResp_id();

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
                } else {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
                    holder.respName.setPaintFlags(holder.respName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.respName.setBackgroundResource(R.color.cardBackgroundFinished);
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.respName.getContext(), R.color.normalImportance));
                    db.profileDao().setFinishedResponsibility(respId);
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
