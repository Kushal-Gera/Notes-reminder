package com.example.reminder.TabFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reminder.R;
import com.example.reminder.SetReminder;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

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
                startActivity(new Intent(getActivity(), SetReminder.class));
            }
        });

        TapTargetView.showFor(getActivity(),
                TapTarget.forView(view.findViewById(R.id.add_btn), "Click Here\nto Add Notes")
                        .cancelable(true)
                        .outerCircleColor(R.color.colorPrimary)
                        .outerCircleAlpha(0.6f)
                        .targetCircleColor(R.color.colorWhite)
                        .targetRadius(50)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .dimColor(R.color.colorBlack)
        );


        return view;


    }



}
