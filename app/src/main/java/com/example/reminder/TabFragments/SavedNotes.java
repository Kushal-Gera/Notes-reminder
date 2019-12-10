package com.example.reminder.TabFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.reminder.MyAdapter;
import com.example.reminder.R;
import com.example.reminder.SQLDatabase.NoteDatabase;
import com.example.reminder.SQLDatabase.NoteUser;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SavedNotes extends Fragment {

    private RecyclerView recyclerView;

    private ImageView savedNotes;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        final View view = inflater.inflate(R.layout.saved_notes, container, false);

        savedNotes = view.findViewById(R.id.savedNotes);

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        final NoteDatabase database =
                Room.databaseBuilder(getActivity(), NoteDatabase.class, "noteTable")
                        .allowMainThreadQueries().build();

        List<NoteUser> list = database.mydao().readData();

        MyAdapter adapter = new MyAdapter(getActivity(), list);


        recyclerView.setAdapter(adapter);

        savedNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);

                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        database.mydao().deleteAll();
                        savedNotes.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }, 1000);

                    }
                }, 2500);

                Snackbar.make(recyclerView, "All Notes Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo    ", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                h.removeCallbacksAndMessages(null);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });

        return view;
    }


}
