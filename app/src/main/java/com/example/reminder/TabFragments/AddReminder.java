package com.example.reminder.TabFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.reminder.App;
import com.example.reminder.MainActivity;
import com.example.reminder.R;

public class AddReminder extends Fragment {
    public static final String FLAG = "flag";


    private TextView changer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_reminder, container, false);

        changer = view.findViewById(R.id.themeChanger);
        changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(FLAG, true);
                startActivity(intent);

                App app = new App();
                app.finish(AddReminder.this);
            }
        });
        return view;
    }



}
