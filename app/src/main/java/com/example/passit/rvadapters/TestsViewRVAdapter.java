package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.R;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TestsViewRVAdapter.VieHolder holder, int position) {
        db = AppDatabase.getDbInstance(holder.testName.getContext());
        int testId = testsList.get(holder.getAdapterPosition()).getTest_id();

        holder.testName.setText(testsList.get(holder.getAdapterPosition()).getTest_name() + "\n\n" + db.profileDao().getSubjectName(testsList.get(position).getSubject_id()));
        holder.testName.setBackgroundResource(R.color.cardBackground);
        holder.progressTV.setText("Niezaliczone");
        holder.progressTV.setTextColor(ContextCompat.getColor(holder.testName.getContext(), R.color.white));

        if (db.profileDao().getTestState(testId)) {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
            //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.testName.setPaintFlags(holder.testName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.testName.setBackgroundResource(R.color.cardBackgroundFinished);
            holder.progressTV.setText("Zaliczone");
            holder.progressTV.setTextColor(ContextCompat.getColor(holder.testName.getContext(), R.color.normalImportance));
        } else {
            holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
            //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.testName.setPaintFlags(holder.testName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.testName.setBackgroundResource(R.color.cardBackground);
            holder.progressTV.setText("Niezaliczone");
            holder.progressTV.setTextColor(ContextCompat.getColor(holder.testName.getContext(), R.color.white));
        }

        switch (testsList.get(holder.getAdapterPosition()).getImportance()) {
            case "normal":
                holder.importanceLabel.setBackgroundResource(R.color.normalImportance);
                break;
            case "medium":
                holder.importanceLabel.setBackgroundResource(R.color.mediumImportance);
                break;
            case "high":
                holder.importanceLabel.setBackgroundResource(R.color.highImportance);
                break;
        }

        holder.markFinishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.profileDao().getTestState(testId)) {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_uncheck_24);
                    //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.testName.setPaintFlags(holder.testName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.testName.setBackgroundResource(R.color.cardBackground);
                    holder.progressTV.setText("Niezaliczone");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.testName.getContext(), R.color.white));
                    db.profileDao().setUnfinishedTest(testId);
                } else {
                    holder.markFinishedBtn.setBackgroundResource(R.drawable.ic_check_24);
                    //holder.subjectName.setPaintFlags(holder.subjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.testName.setPaintFlags(holder.testName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.testName.setBackgroundResource(R.color.cardBackgroundFinished);
                    holder.progressTV.setText("Zaliczone");
                    holder.progressTV.setTextColor(ContextCompat.getColor(holder.testName.getContext(), R.color.normalImportance));
                    db.profileDao().setPassedTest(testId);
                }
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.profileDao().deleteTask(testId);
                testsList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), testsList.size());
            }
        });

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

        private final TextView testName, progressTV;
        private final ConstraintLayout rowLayout;
        private final Button markFinishedBtn, removeBtn;
        private final ImageView importanceLabel;

        public VieHolder(@NonNull View itemView) {
            super(itemView);
            testName = itemView.findViewById(R.id.cv_noteInfo);
            markFinishedBtn = itemView.findViewById(R.id.markFinished);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            rowLayout = itemView.findViewById(R.id.rowLayout);
            importanceLabel = itemView.findViewById(R.id.importanceLabel);
            progressTV = itemView.findViewById(R.id.progressTV);
        }
    }
}
