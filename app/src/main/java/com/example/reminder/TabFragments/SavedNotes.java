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

import com.example.reminder.NoteExtractor;
import com.example.reminder.NoteViewHolder;
import com.example.reminder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavedNotes extends Fragment {
    private static final String TAG = "SavedNotes";
    private static final String NOTE = "note";

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private DatabaseReference ref;

    FirebaseRecyclerOptions<NoteExtractor> options;
    FirebaseRecyclerAdapter<NoteExtractor, NoteViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.saved_notes, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        ref.child("main").child(auth.getCurrentUser().getUid());

        recyclerView = view.findViewById(R.id.recycler_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext() );
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() ));

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

                            String note_text = String.valueOf(dataSnapshot.child("note").getValue());
//                            Toast.makeText(getContext(), note_text, Toast.LENGTH_SHORT).show();
                            holder.saved_title.setText(note_text);

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



}
