package com.example.passit.rvadapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passit.AppDatabase;
import com.example.passit.NoteInfo;
import com.example.passit.R;
import com.example.passit.db.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesViewRVAdapter extends RecyclerView.Adapter<NotesViewRVAdapter.ViewHolder> implements Filterable {

    private final List<Note> noteList;
    private final List<Note> noteListFull;
    private AppDatabase db;

    public NotesViewRVAdapter(List<Note> noteList) {
        this.noteList = noteList;
        noteListFull = new ArrayList<>(noteList);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.noteInfo.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Usuń notatkę");
                builder.setMessage("Czy na pewno chcesz usunąć " + noteList.get(notePosition).getNote_title() + "?");
                builder.setCancelable(false);
                builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                    db.profileDao().deleteNoteWithId(noteId);
                    noteList.remove(notePosition);
                    notifyItemRemoved(notePosition);
                    notifyItemRangeChanged(notePosition, noteList.size());
                    dialog.dismiss();
                });

                builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                /*db.profileDao().deleteNoteWithId(noteId);
                noteList.remove(notePosition);
                notifyItemRemoved(notePosition);
                notifyItemRangeChanged(notePosition, noteList.size());*/
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

    @Override
    public Filter getFilter() {
        return respFilter;
    }

    private Filter respFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(noteListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Note note : noteListFull) {
                    if (note.getNote_title().toLowerCase().contains(filterPattern)) {
                        filteredList.add(note);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            noteList.clear();
            noteList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
