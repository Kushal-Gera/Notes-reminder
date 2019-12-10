package com.example.reminder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView saved_title, saved_desc;
    public ImageView cross, speak;
    public FrameLayout frame_color;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        saved_title = itemView.findViewById(R.id.saved_title);
        saved_desc = itemView.findViewById(R.id.saved_desc);
        cross = itemView.findViewById(R.id.cross);
        speak = itemView.findViewById(R.id.speak);
        frame_color = itemView.findViewById(R.id.frame_color);

    }

}
