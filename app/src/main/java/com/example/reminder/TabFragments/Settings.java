package com.example.reminder.TabFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reminder.BuildConfig;
import com.example.reminder.MainActivity;
import com.example.reminder.R;
import com.google.firebase.auth.FirebaseAuth;

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


        view.findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext() );
                builder.setMessage("Do you really want to sign out ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //nothing
                            }
                        })
                        .setCancelable(true);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView tv = view.findViewById(R.id.version);
        tv.setText(BuildConfig.VERSION_NAME.toString());

        return view;
    }

    private void getPreviousData() {

        SharedPreferences preferences = this.getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        boolean switch_now = preferences.getBoolean(SWITCH_POS, false);

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
