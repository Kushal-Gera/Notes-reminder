package com.example.reminder.TabFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavedNotes extends Fragment {
    private static final String TAG = "SavedNotes";
    private static final String NOTE = "note";
    private static final String DESC_NOTE = "desc_note";

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private DatabaseReference ref;

    LottieAnimationView animationView;

    FirebaseRecyclerOptions<NoteExtractor> options;
    FirebaseRecyclerAdapter<NoteExtractor, NoteViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.saved_notes, container, false);

        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("main").child(auth.getCurrentUser().getUid());

        animationView = view.findViewById(R.id.animation_view);

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext() );
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        options  = new FirebaseRecyclerOptions.Builder<NoteExtractor>()
                         .setQuery(ref, NoteExtractor.class).build();

        adapter = new FirebaseRecyclerAdapter<NoteExtractor, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final NoteViewHolder holder, int i, @NonNull NoteExtractor noteExtractor) {

                final String newNode = getRef(i).getKey();

                if (newNode != null){
                    ref.child(newNode).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String note_text = String.valueOf(dataSnapshot.child(NOTE).getValue());
                            String desc_text = String.valueOf(dataSnapshot.child(DESC_NOTE).getValue());
                            animationView.setVisibility(View.GONE);

                            holder.saved_title.setText(note_text);
                            holder.saved_desc.setText(desc_text);

                            holder.cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ref.child(newNode).removeValue();
                                }
                            });

                            holder.speak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    speak_now();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: ERROR OCCURRED" );
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

        return view;
    }

    private void speak_now() {




    }


}
