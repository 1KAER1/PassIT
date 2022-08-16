package com.example.passit.rvadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.R;

import java.util.ArrayList;

public class LabSummaryRVAdapter extends RecyclerView.Adapter<LabSummaryRVAdapter.ViewHolder> {
    private final ArrayList<String> labSummaryRVModalArrayList;
    private final ArrayList<String> labSummaryRVModalArrayList2;

    public LabSummaryRVAdapter(ArrayList<String> labSummaryRVModalArrayList, ArrayList<String> labSummaryRVModalArrayList2) {
        this.labSummaryRVModalArrayList = labSummaryRVModalArrayList;
        this.labSummaryRVModalArrayList2 = labSummaryRVModalArrayList2;
    }


    @NonNull
    @Override
    public LabSummaryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_summary_row, parent, false);
        return new LabSummaryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabSummaryRVAdapter.ViewHolder holder, int position) {
        holder.selectedDay.setText(labSummaryRVModalArrayList.get(position));
        holder.selectedHours.setText(labSummaryRVModalArrayList2.get(position));
    }

    @Override
    public int getItemCount() {
        return labSummaryRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView selectedDay;
        private final TextView selectedHours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedDay = itemView.findViewById(R.id.dayName);
            selectedHours = itemView.findViewById(R.id.classesHour);
        }
    }
}
