package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
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
    private final ArrayList<Integer> labSummaryRVModalArrayList3;

    public LabSummaryRVAdapter(ArrayList<String> labSummaryRVModalArrayList,
                               ArrayList<String> labSummaryRVModalArrayList2,
                               ArrayList<Integer> labSummaryRVModalArrayList3) {
        this.labSummaryRVModalArrayList = labSummaryRVModalArrayList;
        this.labSummaryRVModalArrayList2 = labSummaryRVModalArrayList2;
        this.labSummaryRVModalArrayList3 = labSummaryRVModalArrayList3;
    }


    @NonNull
    @Override
    public LabSummaryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_summary_row, parent, false);
        return new LabSummaryRVAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LabSummaryRVAdapter.ViewHolder holder, int position) {
        holder.selectedDay.setText(labSummaryRVModalArrayList.get(position));
        holder.selectedHours.setText(labSummaryRVModalArrayList2.get(position));
        holder.classPeriod.setText(labSummaryRVModalArrayList3.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return labSummaryRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView selectedDay;
        private final TextView selectedHours;
        private final TextView classPeriod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedDay = itemView.findViewById(R.id.subjectName);
            selectedHours = itemView.findViewById(R.id.ectsTv);
            classPeriod = itemView.findViewById(R.id.classPeriod);
        }
    }
}
