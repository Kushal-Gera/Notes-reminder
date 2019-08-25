package com.example.reminder.TabFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reminder.MainActivity;
import com.example.reminder.R;

public class TakeNotes extends Fragment {
    private static final String TAG = "TakeNotes";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button add_btn;
        View view = inflater.inflate(R.layout.take_note, container, false);

        add_btn = view.findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "add btn clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }


}
