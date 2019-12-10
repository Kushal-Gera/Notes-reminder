package com.example.reminder;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.example.reminder.SQLDatabase.NoteDatabase;
import com.example.reminder.SQLDatabase.NoteUser;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SetReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "SetReminder";

    public static final String SHARED_PREF = "shared_preference";
    private static final String ITEM_ID = "item_id";
    private static boolean IS_DARK = false;

    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String IS_SAVED = "is_saved";

    private NoteDatabase database;


    Button save, alarm;
    EditText note, note_desc;
    ImageView listen, swap;

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


        database =
                Room.databaseBuilder(this, NoteDatabase.class, "noteTable")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build();

        save = findViewById(R.id.save);
        swap = findViewById(R.id.swap);
        alarm = findViewById(R.id.alarm);
        note = findViewById(R.id.note);
        note_desc = findViewById(R.id.note_desc);
        listen = findViewById(R.id.listen);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String note_string = note.getText().toString().trim();
                String desc = note_desc.getText().toString().trim();

                if (!TextUtils.isEmpty(note_string)) {
                    //do something
                    if (TextUtils.isEmpty(note_desc.getText()))
                        desc = "";

                    saveData(note_string, desc);
                    Toast.makeText(SetReminder.this, "Saved", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else
                    note.setError("Required !!");
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String note_string = note.getText().toString().trim();
                String desc = note_desc.getText().toString().trim();
                if (!TextUtils.isEmpty(note_string)) {

                    if (TextUtils.isEmpty(note_desc.getText()))
                        desc = "";

                    saveData(note_string, desc);

                    DialogFragment dialogFragment = new TimePickerFrag();
                    dialogFragment.show(getSupportFragmentManager(), "anything");
                } else
                    note.setError("Required !!");
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen_now();
            }
        });

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap_it();
            }
        });


        String s_title = getIntent().getStringExtra(TITLE);
        String s_text = getIntent().getStringExtra(TEXT);
        note.setText(s_title);
        note_desc.setText(s_text);

        SharedPreferences pref = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        boolean first = pref.getBoolean("first_time", true);

        if (first) {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.listen), "Save Notes With Voice")
                                    .cancelable(true)
                                    .outerCircleColor(R.color.colorPrimary)
                                    .outerCircleAlpha(0.6f)
                                    .targetCircleColor(R.color.colorWhite)
                                    .targetRadius(50)
                                    .cancelable(false)
                                    .tintTarget(true)
                                    .transparentTarget(true)
                                    .dimColor(R.color.colorBlack),
                            TapTarget.forView(findViewById(R.id.save), "Tap to save")
                                    .cancelable(true)
                                    .outerCircleColor(R.color.colorPrimary)
                                    .outerCircleAlpha(0.6f)
                                    .targetCircleColor(R.color.colorWhite)
                                    .tintTarget(true)
                                    .cancelable(false)
                                    .targetRadius(50)
                                    .transparentTarget(true)
                                    .dimColor(R.color.colorBlack),
                            TapTarget.forView(findViewById(R.id.alarm), "Save as Reminder With Alarm")
                                    .cancelable(true)
                                    .outerCircleColor(R.color.colorPrimary)
                                    .outerCircleAlpha(0.6f)
                                    .targetCircleColor(R.color.colorWhite)
                                    .tintTarget(true)
                                    .targetRadius(50)
                                    .transparentTarget(true)
                                    .dimColor(R.color.colorBlack)
                    ).start();

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("first_time", false).apply();
        }


    }

    private void swap_it() {

        String temp = "";

        temp = note.getText().toString();
        note.setText(note_desc.getText().toString());
        note_desc.setText(temp);

    }

    private void saveData(String data, String desc) {
        NoteUser user = new NoteUser();

        if (!(getIntent().getBooleanExtra(IS_SAVED, false))) {  //new note
            user.setNote(data);
            user.setDesc(desc);
            int i = new Random().nextInt(100);
            user.setColor(i % 4);

            database.mydao().insert(user);

        } else {    //saved note being updated
            database.mydao().updateNote(data, desc, getIntent().getIntExtra("id", 0));
        }

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
        finish();

    }

    private void listen_now() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something !");

        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "ERROR\n" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null) {

            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            note.setText(result.get(0));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
