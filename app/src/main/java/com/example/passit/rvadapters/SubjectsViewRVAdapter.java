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
import com.example.passit.SubjectDetails;
import com.example.passit.SubjectsView;
import com.example.passit.db.entities.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsViewRVAdapter extends RecyclerView.Adapter<SubjectsViewRVAdapter.ViewHolder> {

    private final List<Subject> subjectNameList1;
    private AppDatabase db;

    public SubjectsViewRVAdapter(List<Subject> subjectNameList1) {
        this.subjectNameList1 = subjectNameList1;
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

        if (db.profileDao().getSubjectState(subjectId)) {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
            holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.subjectName.setBackgroundResource(R.color.cardBackgroundFinished);
        } else {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
            holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.subjectName.setBackgroundResource(R.color.cardBackground);
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
                    db.profileDao().setSubjectInProgress(subjectId);
                } else {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
                    holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.subjectName.setBackgroundResource(R.color.cardBackgroundFinished);
                    db.profileDao().setPassedSubject(subjectId);
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.profileDao().deleteSubject(subjectId);
                subjectNameList1.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), subjectNameList1.size());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectName;
        private final ConstraintLayout rowLayout;
        private final Button markFinishedBtn, removeBtn;
        private final ImageView importanceLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.cv_subjectName);
            markFinishedBtn = itemView.findViewById(R.id.markFinished);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            rowLayout = itemView.findViewById(R.id.rowLayout);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
        }
    }
}
