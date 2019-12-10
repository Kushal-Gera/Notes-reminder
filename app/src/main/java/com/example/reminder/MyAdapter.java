package com.example.reminder;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.reminder.SQLDatabase.NoteDatabase;
import com.example.reminder.SQLDatabase.NoteUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

@SuppressWarnings("deprecation")
public class MyAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;
    private List<NoteUser> list = new ArrayList<>();
    private TextToSpeech tts;
    private NoteDatabase database;

    private final int[] colorArray = new int[]{
            R.color.pink_note,
            R.color.orange_note,
            R.color.yellow_note,
            R.color.green_note};

    public MyAdapter(Context context) {
        this.context = context;

        database = Room.databaseBuilder(context, NoteDatabase.class, "noteTable")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.saved_note_list, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {

        holder.saved_desc.setText(list.get(position).getDesc());
        holder.saved_title.setText(list.get(position).getNote());
        holder.frame_color.setBackground(context.getDrawable(colorArray[list.get(position).getColor()]));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SetReminder.class);
                intent.putExtra("is_saved", true);

                intent.putExtra("color", list.get(position).getColor());
                intent.putExtra("title", list.get(position).getNote());
                intent.putExtra("text", list.get(position).getDesc());
                intent.putExtra("id", list.get(position).getId());

                context.startActivity(intent);
            }
        });

        holder.speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            speak(list.get(position).getNote());
                        }
                    }
                });
            }
        });


        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemRemoved(position);
                database.mydao().delete(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    private void speak(String data) {
        tts.setLanguage(Locale.getDefault());
        tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void setData(List<NoteUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}
