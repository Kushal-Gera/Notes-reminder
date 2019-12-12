package com.example.reminder.TabFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build();

        adapter = new MyAdapter(getContext());
        recyclerView.setAdapter(adapter);

        database.mydao().readData().observe(this, new Observer<List<NoteUser>>() {
            @Override
            public void onChanged(final List<NoteUser> listtt) {
                adapter.setData(listtt);

                view.findViewById(R.id.text_view3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchInData("hello", listtt);
                    }
                });

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

    private void searchInData(String text, List<NoteUser> data) {

        int found = 0;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getNote().contains(text)){
                found = 2;
                break;
            }
            else if (data.get(i).getDesc().contains(text)){
                found = 1;
                break;
            }
        }


        if (found == 2)
            Toast.makeText(getActivity(), "Note, got -> " + text, Toast.LENGTH_LONG).show();
        else if (found == 1)
            Toast.makeText(getActivity(), "Desc, got -> " + text, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getActivity(), "nothing found !!", Toast.LENGTH_LONG).show();

    }

}


