package com.example.reminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.example.reminder.TabFragments.AddReminder;
import com.example.reminder.TabFragments.SavedNotes;
import com.example.reminder.TabFragments.TakeNotes;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String SHARED_PREF = "shared_preference";
    private static final String ITEM_ID = "item_id";
    private static boolean IS_DARK = false;

    TabLayout tabs;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //change this to Delegate or shared preference.......
        getSharedPrefData();
        if (IS_DARK)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        addtabs();
        tabs.setupWithViewPager(viewPager);




    }

    private void getSharedPrefData() {

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        IS_DARK = preferences.getBoolean(ITEM_ID, false);


    }

    private void addtabs(){
        FragPagerAdapter adapter = new FragPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new TakeNotes(), "Take Notes");
        adapter.addFrag(new SavedNotes(), "Saved Notes");
        adapter.addFrag(new AddReminder(), "Reminder");
        viewPager.setAdapter(adapter);
    }




}
