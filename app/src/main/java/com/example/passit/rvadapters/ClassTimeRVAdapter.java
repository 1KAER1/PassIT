package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
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
    private final ArrayList<Integer> classTimeRVModalArrayList3;

    public ClassTimeRVAdapter(ArrayList<String> classTimeRVModalArrayList, ArrayList<String> classTimeRVModalArrayList2, ArrayList<Integer> classTimeRVModalArrayList3) {
        this.classTimeRVModalArrayList = classTimeRVModalArrayList;
        this.classTimeRVModalArrayList2 = classTimeRVModalArrayList2;
        this.classTimeRVModalArrayList3 = classTimeRVModalArrayList3;
    }

    @NonNull
    @Override
    public ClassTimeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClassTimeRVAdapter.ViewHolder holder, int position) {
        holder.classTimeTV1.setText(classTimeRVModalArrayList.get(position));
        holder.classTimeTV2.setText(classTimeRVModalArrayList2.get(position));
        holder.classTimeTV3.setText(classTimeRVModalArrayList3.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return classTimeRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView classTimeTV1;
        private final TextView classTimeTV2;
        private final TextView classTimeTV3;
        private final Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            removeButton = itemView.findViewById(R.id.removeFromRV);
            classTimeTV1 = itemView.findViewById(R.id.subjectName);
            classTimeTV2 = itemView.findViewById(R.id.ectsTv);
            classTimeTV3 = itemView.findViewById(R.id.classPeriod);

            removeButton.setOnClickListener(view -> {
                classTimeRVModalArrayList.remove(getAdapterPosition());
                classTimeRVModalArrayList2.remove(getAdapterPosition());
                classTimeRVModalArrayList3.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), classTimeRVModalArrayList.size());
            });
        }
    }

}
