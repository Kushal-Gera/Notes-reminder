package com.example.reminder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView saved_title, saved_desc;
    public ImageView cross, speak;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        saved_desc = itemView.findViewById(R.id.saved_desc);
        saved_title = itemView.findViewById(R.id.saved_title);
        cross = itemView.findViewById(R.id.cross);
        speak = itemView.findViewById(R.id.speak);

    }

}
