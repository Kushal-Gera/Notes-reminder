package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.ETC1;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SetReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "SetReminder";

    private static final String NOTE = "note";
    public static final String SHARED_PREF = "shared_preference";
    private static final String ITEM_ID = "item_id";
    private static boolean IS_DARK = false;

    //firebase stuff
    FirebaseAuth auth;
    DatabaseReference ref;

    Button save, alarm;
    EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //change this to shared preference.......
        getSharedPrefData();
        if (IS_DARK)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_set_reminder);

        // initialisations

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        save = findViewById(R.id.save);
        alarm = findViewById(R.id.alarm);
        note = findViewById(R.id.note);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String note_string = note.getText().toString().trim();

                if (!TextUtils.isEmpty(note_string)){
                    //do something
                    saveData(note_string);
                    Toast.makeText(SetReminder.this, "Saved", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else Toast.makeText(SetReminder.this,"Please Add Something", Toast.LENGTH_SHORT).show();

            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String note_string = note.getText().toString().trim();
                if (!TextUtils.isEmpty(note_string)) {
                    saveData(note_string);

                    DialogFragment dialogFragment = new TimePickerFrag();
                    dialogFragment.show(getSupportFragmentManager(), "anything");
                }
            }
        });

    }

    private void saveData(String data) {

        ref.child("main").child(auth.getCurrentUser().getUid()).push().child(NOTE).setValue(data);

    }

    private void getSharedPrefData() {

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        IS_DARK = preferences.getBoolean(ITEM_ID, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        startActivity(intent);
    }






}
