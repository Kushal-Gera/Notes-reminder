package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.reminder.TabFragments.AddReminder;
import com.example.reminder.TabFragments.SavedNotes;
import com.example.reminder.TabFragments.TakeNotes;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        addtabs();
        tabs.setupWithViewPager(viewPager);

    }

    private void addtabs(){
        FragPagerAdapter adapter = new FragPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new TakeNotes(), "Take Notes");
        adapter.addFrag(new SavedNotes(), "Saved Notes");
        adapter.addFrag(new AddReminder(), "Add Reminder");
        viewPager.setAdapter(adapter);
    }




}
