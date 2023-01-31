package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.db.AppDatabase;
import com.example.passit.R;
import com.example.passit.ResponsibilityInfo;
import com.example.passit.db.entities.Responsibility;

import java.util.List;

public class ResponsibilitiesMainRVAdapter extends RecyclerView.Adapter<ResponsibilitiesMainRVAdapter.ViewHolder> {

    private final List<Responsibility> responsibilitiesList;
    private AppDatabase db;

    public ResponsibilitiesMainRVAdapter(List<Responsibility> responsibilitiesList) {
        this.responsibilitiesList = responsibilitiesList;
    }

    @NonNull
    @Override
    public ResponsibilitiesMainRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_horizontal_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResponsibilitiesMainRVAdapter.ViewHolder holder, int position) {
        db = AppDatabase.getDbInstance(holder.respName.getContext());
        int pos = holder.getAdapterPosition();
        int respId = responsibilitiesList.get(pos).getResp_id();

        holder.respName.setText(responsibilitiesList.get(pos).getResp_name() + "\n"
                + db.profileDao().getSubjectName(responsibilitiesList.get(pos).getSubject_id())
                + "\nTermin: " + responsibilitiesList.get(pos).getDate_due());
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

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ResponsibilityInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("respId", respId);
            bundle.putString("menu", "isMenu");
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return responsibilitiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView respName, progressTV;
        private final ImageView importanceLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            respName = itemView.findViewById(R.id.cv_noteInfo);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
            progressTV = itemView.findViewById(R.id.progressTV);
        }
    }
}
