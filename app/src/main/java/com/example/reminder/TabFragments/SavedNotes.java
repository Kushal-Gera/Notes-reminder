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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.reminder.MyAdapter;
import com.example.reminder.R;
import com.example.reminder.SQLDatabase.NoteDatabase;
import com.example.reminder.SQLDatabase.NoteUser;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

public class SavedNotes extends Fragment {

    private RecyclerView recyclerView;
    private ImageView savedNotes;
    MyAdapter adapter;


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
                Room.databaseBuilder(Objects.requireNonNull(getActivity()), NoteDatabase.class, "noteTable")
                        .allowMainThreadQueries()
                        .allowMainThreadQueries().build();

        adapter = new MyAdapter(getContext());
        recyclerView.setAdapter(adapter);


        LiveData<List<NoteUser>> liveData = database.mydao().readData();
        liveData.observe(this, new Observer<List<NoteUser>>() {
            @Override
            public void onChanged(List<NoteUser> listtt) {
                adapter.setData(listtt);
            }
        });


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
