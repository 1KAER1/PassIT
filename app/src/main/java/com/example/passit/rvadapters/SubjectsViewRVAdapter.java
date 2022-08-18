package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.R;
import com.example.passit.db.entities.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsViewRVAdapter extends RecyclerView.Adapter<SubjectsViewRVAdapter.ViewHolder> {

    private final List<Subject> subjectNameList1;

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
        holder.subjectName.setText(subjectNameList1.get(position).getSubject_name());
        holder.ectsPoints.setText(subjectNameList1.get(position).getEcts_points() + " ECTS");
        if (subjectNameList1.get(position).getImportance().equals("normal")) {
            holder.rowLayout.setBackgroundResource(R.drawable.normal_importance_gradient);
            //holder.subjectName.setTextColor(ContextCompat.getColor(this, R.color.background));
        } else if (subjectNameList1.get(position).getImportance().equals("medium")) {
            holder.rowLayout.setBackgroundResource(R.drawable.medium_importance_gradient);
        } else if (subjectNameList1.get(position).getImportance().equals("high")) {
            holder.rowLayout.setBackgroundResource(R.drawable.high_importance_gradient);
        }
    }

    @Override
    public int getItemCount() {
        return subjectNameList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectName;
        private final TextView ectsPoints;
        private final ConstraintLayout rowLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            ectsPoints = itemView.findViewById(R.id.ectsTv);
            rowLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
