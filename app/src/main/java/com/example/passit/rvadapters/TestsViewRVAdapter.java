package com.example.passit.rvadapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.R;
import com.example.passit.SubjectDetails;
import com.example.passit.TestInfo;
import com.example.passit.db.entities.Test;

import java.util.List;

public class TestsViewRVAdapter extends RecyclerView.Adapter<TestsViewRVAdapter.VieHolder> {
    private final List<Test> testsList;
    private AppDatabase db;

    public TestsViewRVAdapter(List<Test> testsList) {
        this.testsList = testsList;
    }

    @NonNull
    @Override
    public TestsViewRVAdapter.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_test_row, parent, false);
        return new VieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsViewRVAdapter.VieHolder holder, int position) {
        db = AppDatabase.getDbInstance(holder.subjectName.getContext());
        int testId = testsList.get(position).getTest_id();

        holder.subjectName.setText(db.profileDao().getSubjectName(testsList.get(position).getSubject_id()));
        holder.testName.setText(testsList.get(position).getTest_name());
        holder.dateTV.setText(testsList.get(position).getDate_due());
        holder.timeTV.setText(testsList.get(position).getHour_due());

        if (testsList.get(position).getImportance().equals("normal")) {
            holder.rowLayout.setBackgroundResource(R.drawable.normal_importance_gradient);
            //holder.subjectName.setTextColor(ContextCompat.getColor(this, R.color.background));
        } else if (testsList.get(position).getImportance().equals("medium")) {
            holder.rowLayout.setBackgroundResource(R.drawable.medium_importance_gradient);
        } else if (testsList.get(position).getImportance().equals("high")) {
            holder.rowLayout.setBackgroundResource(R.drawable.high_importance_gradient);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), TestInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("testId", testId);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }


    public class VieHolder extends RecyclerView.ViewHolder {

        private final TextView subjectName, testName, dateTV, timeTV;
        private final ConstraintLayout rowLayout;

        public VieHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            testName = itemView.findViewById(R.id.testName);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            rowLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
