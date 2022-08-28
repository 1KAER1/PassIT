package com.example.passit.rvadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.R;
import com.example.passit.db.entities.LessonDate;

import java.util.List;

public class LectureInfoRVAdapter extends RecyclerView.Adapter<LectureInfoRVAdapter.ViewHolder> {

    private final List<LessonDate> lessonDates;

    public LectureInfoRVAdapter(List<LessonDate> lessonDates) {
        this.lessonDates = lessonDates;
    }

    @NonNull
    @Override
    public LectureInfoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_summary_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureInfoRVAdapter.ViewHolder holder, int position) {
        holder.selectedDay.setText(lessonDates.get(position).getDay_name());
        holder.selectedHours.setText(lessonDates.get(position).getLesson_time());
        holder.classPeriod.setText(String.valueOf(lessonDates.get(position).getLesson_period()));
    }

    @Override
    public int getItemCount() {
        return lessonDates.size();
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
