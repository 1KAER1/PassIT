package com.example.passit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassTimeRVAdapter extends RecyclerView.Adapter<ClassTimeRVAdapter.ViewHolder> {
    private final int rvItemsLimit = 7;

    private ArrayList<String> classTimeRVModalArrayList;
    private ArrayList<String> classTimeRVModalArrayList2;

    public ClassTimeRVAdapter(ArrayList<String> classTimeRVModalArrayList, ArrayList<String> classTimeRVModalArrayList2) {
        this.classTimeRVModalArrayList = classTimeRVModalArrayList;
        this.classTimeRVModalArrayList2 = classTimeRVModalArrayList2;
    }

    @NonNull
    @Override
    public ClassTimeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassTimeRVAdapter.ViewHolder holder, int position) {
        holder.classTimeTV1.setText(classTimeRVModalArrayList.get(position));
        holder.classTimeTV2.setText(classTimeRVModalArrayList2.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(classTimeRVModalArrayList.size(), rvItemsLimit);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView classTimeTV1;
        private final TextView classTimeTV2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classTimeTV1 = itemView.findViewById(R.id.dayName);
            classTimeTV2 = itemView.findViewById(R.id.classesHour);
        }
    }

}
