package com.example.reminder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView saved_title;
    public TextView saved_text;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        saved_title = itemView.findViewById(R.id.saved_title);
        saved_text = itemView.findViewById(R.id.saved_text);

    }

}
