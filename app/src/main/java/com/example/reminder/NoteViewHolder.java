package com.example.reminder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView saved_title;
    public ImageView cross;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        saved_title = itemView.findViewById(R.id.saved_title);
        cross = itemView.findViewById(R.id.cross);

    }

}