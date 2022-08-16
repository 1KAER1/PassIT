package com.example.passit.rvadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.R;

import java.util.ArrayList;

public class ClassTimeRVAdapter extends RecyclerView.Adapter<ClassTimeRVAdapter.ViewHolder> {

    private final ArrayList<String> classTimeRVModalArrayList;
    private final ArrayList<String> classTimeRVModalArrayList2;

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
        return classTimeRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView classTimeTV1;
        private final TextView classTimeTV2;
        private final Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            removeButton = itemView.findViewById(R.id.removeFromRV);
            classTimeTV1 = itemView.findViewById(R.id.dayName);
            classTimeTV2 = itemView.findViewById(R.id.classesHour);

            removeButton.setOnClickListener(view -> {
                classTimeRVModalArrayList.remove(getAdapterPosition());
                classTimeRVModalArrayList2.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), classTimeRVModalArrayList.size());
            });
        }
    }

}
