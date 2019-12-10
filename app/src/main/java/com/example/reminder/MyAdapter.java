package com.example.reminder;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.SQLDatabase.NoteUser;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;
    private List<NoteUser> list;

    public MyAdapter(Context context, List<NoteUser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.saved_note_list, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        holder.saved_desc.setText(list.get(position).getDesc());
        holder.saved_title.setText(list.get(position).getNote());


        holder.speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                TextToSpeech tts = new TextToSpeech(this, );
//                tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
