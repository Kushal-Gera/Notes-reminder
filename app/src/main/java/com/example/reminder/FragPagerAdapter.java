package com.example.reminder;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragList = new ArrayList<>();
    private List<String> fragTitleList = new ArrayList<>();

    public FragPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitleList.get(position);
    }

    public void addFrag(Fragment fragment, String title){

        fragList.add(fragment);
        fragTitleList.add(title);

    }


}
