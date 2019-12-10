package com.example.reminder.TabFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.reminder.MyAdapter;
import com.example.reminder.NoteExtractor;
import com.example.reminder.NoteViewHolder;
import com.example.reminder.R;
import com.example.reminder.SQLDatabase.NoteDatabase;
import com.example.reminder.SQLDatabase.NoteUser;
import com.example.reminder.SetReminder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SavedNotes extends Fragment {
    private static final String TAG = "SavedNotes";
    private static final String NOTE = "note";
    private static final String DESC_NOTE = "desc_note";
    private static final String COLOR = "color";

    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String NODE = "node";
    private static final String IS_SAVED = "is_saved";

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private DatabaseReference ref;

    private TextToSpeech tts;

    private ImageView savedNotes;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        final View view = inflater.inflate(R.layout.saved_notes, container, false);

        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance()
                .getReference().child("main")
                .child(auth.getCurrentUser().getUid());

        savedNotes = view.findViewById(R.id.savedNotes);

        final int[] colorArray = new int[]{
                R.color.pink_note,
                R.color.orange_note,
                R.color.yellow_note,
                R.color.green_note};

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
//                        ref.removeValue();
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

    private void speak_now(final String data) {

        tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void onStop() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onStop();
    }


}
