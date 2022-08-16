package com.example.passit.rvadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.R;

import java.util.ArrayList;

public class ExerciseSummaryRVAdapter extends RecyclerView.Adapter<ExerciseSummaryRVAdapter.ViewHolder> {

    private final ArrayList<String> exerciseSummaryRVModalArrayList;
    private final ArrayList<String> exerciseSummaryRVModalArrayList2;

    public ExerciseSummaryRVAdapter(ArrayList<String> exerciseSummaryRVModalArrayList, ArrayList<String> exerciseSummaryRVModalArrayList2) {
        this.exerciseSummaryRVModalArrayList = exerciseSummaryRVModalArrayList;
        this.exerciseSummaryRVModalArrayList2 = exerciseSummaryRVModalArrayList2;
    }

    @NonNull
    @Override
    public ExerciseSummaryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_summary_row, parent, false);
        return new ExerciseSummaryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSummaryRVAdapter.ViewHolder holder, int position) {
        holder.selectedDay.setText(exerciseSummaryRVModalArrayList.get(position));
        holder.selectedHours.setText(exerciseSummaryRVModalArrayList2.get(position));
    }

    @Override
    public int getItemCount() {
        return exerciseSummaryRVModalArrayList.size();
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
