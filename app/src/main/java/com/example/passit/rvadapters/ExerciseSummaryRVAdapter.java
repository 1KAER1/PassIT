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

public class ExerciseSummaryRVAdapter extends RecyclerView.Adapter<ExerciseSummaryRVAdapter.ViewHolder> {

    private final ArrayList<String> exerciseSummaryRVModalArrayList;
    private final ArrayList<String> exerciseSummaryRVModalArrayList2;
    private final ArrayList<Integer> exerciseSummaryRVModalArrayList3;

    public ExerciseSummaryRVAdapter(ArrayList<String> exerciseSummaryRVModalArrayList,
                                    ArrayList<String> exerciseSummaryRVModalArrayList2,
                                    ArrayList<Integer> exerciseSummaryRVModalArrayList3) {
        this.exerciseSummaryRVModalArrayList = exerciseSummaryRVModalArrayList;
        this.exerciseSummaryRVModalArrayList2 = exerciseSummaryRVModalArrayList2;
        this.exerciseSummaryRVModalArrayList3 = exerciseSummaryRVModalArrayList3;
    }

    @NonNull
    @Override
    public ExerciseSummaryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_summary_row, parent, false);
        return new ExerciseSummaryRVAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerciseSummaryRVAdapter.ViewHolder holder, int position) {
        holder.selectedDay.setText(exerciseSummaryRVModalArrayList.get(position));
        holder.selectedHours.setText(exerciseSummaryRVModalArrayList2.get(position));
        holder.classPeriod.setText(exerciseSummaryRVModalArrayList3.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return exerciseSummaryRVModalArrayList.size();
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
