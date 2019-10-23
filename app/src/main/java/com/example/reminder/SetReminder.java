package com.example.reminder;

import android.app.TimePickerDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SetReminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "SetReminder";

    private static final String NOTE = "note";
    private static final String DESC_NOTE = "desc_note";
    public static final String SHARED_PREF = "shared_preference";
    private static final String ITEM_ID = "item_id";
    private static boolean IS_DARK = false;

    //firebase stuff
    FirebaseAuth auth;
    DatabaseReference ref;

    Button save, alarm;
    EditText note, note_desc;
    ImageView listen;

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
        note_desc = findViewById(R.id.note_desc);
        listen = findViewById(R.id.listen);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String note_string = note.getText().toString().trim();
                String desc = note_desc.getText().toString().trim();

                if (!TextUtils.isEmpty(note_string)){
                    //do something
                    if (TextUtils.isEmpty(note_desc.getText()))
                        desc = "";

                    saveData(note_string, desc);
                    Toast.makeText(SetReminder.this, "Saved", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else
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
                }
                else
                    note.setError("Required !!");
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen_now();
            }
        });


    }

    private void saveData(String data, String desc) {

        DatabaseReference new_ref = ref.child("main").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).push();
        new_ref.child(NOTE).setValue(data);
        new_ref.child(DESC_NOTE).setValue(desc);

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

    private void listen_now() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something !");

        try {

            startActivityForResult(intent, 1);

        }catch (Exception e){
            Toast.makeText(this, "ERROR\n" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data!=null){

            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            note.setText(result.get(0).toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}
