package com.example.passit.rvadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.R;

import java.util.ArrayList;

public class SummaryRVAdapter extends RecyclerView.Adapter<SummaryRVAdapter.ViewHolder> {

    private final ArrayList<String> summaryRVModalArrayList;
    private final ArrayList<String> summaryRVModalArrayList2;

    public SummaryRVAdapter(ArrayList<String> summaryRVModalArrayList, ArrayList<String> summaryRVModalArrayList2) {
        this.summaryRVModalArrayList = summaryRVModalArrayList;
        this.summaryRVModalArrayList2 = summaryRVModalArrayList2;
    }

    @NonNull
    @Override
    public SummaryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_summary_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryRVAdapter.ViewHolder holder, int position) {
        holder.selectedDay.setText(summaryRVModalArrayList.get(position));
        holder.selectedHours.setText(summaryRVModalArrayList2.get(position));
    }

    @Override
    public int getItemCount() {
        return summaryRVModalArrayList.size();
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
