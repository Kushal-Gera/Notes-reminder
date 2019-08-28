package com.example.reminder.TabFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reminder.MainActivity;
import com.example.reminder.R;

public class Settings extends Fragment {
    public static final String SHARED_PREF = "shared_preference";
    private static final String ITEM_ID = "item_id";
    private static final String SWITCH_POS = "switch";

    private Switch themeSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings, container, false);

        themeSwitch = view.findViewById(R.id.themeSwitch);
        getPreviousData();

        themeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();

            }
        });
        return view;
    }

    private void getPreviousData() {

        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        boolean switch_now = preferences.getBoolean(SWITCH_POS, true);

        themeSwitch.setChecked(switch_now);
    }

    private void changeTheme(){

        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (themeSwitch.isChecked() ){

            editor.putBoolean(ITEM_ID, true);
            editor.putBoolean(SWITCH_POS, true);
        }
        else{
            editor.putBoolean(ITEM_ID, false);
            editor.putBoolean(SWITCH_POS, false);
        }

        editor.apply();
    }

}
