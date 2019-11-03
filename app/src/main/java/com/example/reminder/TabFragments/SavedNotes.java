package com.example.reminder.TabFragments;

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

import com.airbnb.lottie.LottieAnimationView;
import com.example.reminder.NoteExtractor;
import com.example.reminder.NoteViewHolder;
import com.example.reminder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavedNotes extends Fragment {
    private static final String TAG = "SavedNotes";
    private static final String NOTE = "note";
    private static final String DESC_NOTE = "desc_note";
    private static final String COLOR = "color";

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private DatabaseReference ref;

    private LottieAnimationView animationView;

    private FirebaseRecyclerOptions<NoteExtractor> options;
    private FirebaseRecyclerAdapter<NoteExtractor, NoteViewHolder> adapter;

    private TextToSpeech tts;

    private ImageView savedNotes;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.saved_notes, container, false);

        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("main").child(auth.getCurrentUser().getUid());

        animationView = view.findViewById(R.id.animation_view);
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

        options = new FirebaseRecyclerOptions.Builder<NoteExtractor>()
                .setQuery(ref, NoteExtractor.class).build();

        adapter = new FirebaseRecyclerAdapter<NoteExtractor, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final NoteViewHolder holder, final int i, @NonNull NoteExtractor noteExtractor) {

                final String newNode = getRef(i).getKey();

                if (newNode != null) {
                    savedNotes.setVisibility(View.VISIBLE);
                    ref.child(newNode).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final String note_text = String.valueOf(dataSnapshot.child(NOTE).getValue());
                            final String desc_text = String.valueOf(dataSnapshot.child(DESC_NOTE).getValue());
                            animationView.setVisibility(View.GONE);

                            holder.saved_title.setText(note_text);
                            holder.saved_desc.setText(desc_text);

                            try {
                                int r = Integer.parseInt(String.valueOf(dataSnapshot.child(COLOR).getValue()));
                                holder.frame_color
                                        .setBackgroundColor(getContext().getResources().getColor(colorArray[r]));
                            }
                            catch (NullPointerException | NumberFormatException e) {
                                e.printStackTrace();
                            }

                            holder.cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ref.child(newNode).removeValue();
                                }
                            });

                            holder.speak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int status) {

                                            if (status != TextToSpeech.SUCCESS)
                                                return;
                                            speak_now(note_text);
                                        }
                                    });

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: ERROR OCCURRED");
                        }
                    });

                }
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View this_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_note_list, parent, false);
                return new NoteViewHolder(this_view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        savedNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);

                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ref.removeValue();
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
