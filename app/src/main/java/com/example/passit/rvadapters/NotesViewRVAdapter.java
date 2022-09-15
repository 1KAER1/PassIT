package com.example.passit.rvadapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.NoteInfo;
import com.example.passit.R;
import com.example.passit.TaskInfo;
import com.example.passit.db.entities.Note;

import org.w3c.dom.Text;

import java.util.List;

public class NotesViewRVAdapter extends RecyclerView.Adapter<NotesViewRVAdapter.ViewHolder> {

    private final List<Note> noteList;
    private AppDatabase db;

    public NotesViewRVAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NotesViewRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_note_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewRVAdapter.ViewHolder holder, int position) {

        db = AppDatabase.getDbInstance(holder.noteInfo.getContext());
        int notePosition = holder.getAdapterPosition();

        int noteId = noteList.get(notePosition).getNote_id();

        holder.noteInfo.setText(noteList.get(notePosition).getNote_title());

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.profileDao().deleteTask(noteId);
                noteList.remove(notePosition);
                notifyItemRemoved(notePosition);
                notifyItemRangeChanged(notePosition, noteList.size());
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), NoteInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("noteId", noteId);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView noteInfo;
        private final Button removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteInfo = itemView.findViewById(R.id.cv_noteInfo);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}
