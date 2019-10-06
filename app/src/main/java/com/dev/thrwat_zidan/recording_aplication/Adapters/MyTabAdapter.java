package com.dev.thrwat_zidan.recording_aplication.Adapters;

import com.dev.thrwat_zidan.recording_aplication.Fragments.FileViewFragment;
import com.dev.thrwat_zidan.recording_aplication.Fragments.RecordFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyTabAdapter extends FragmentPagerAdapter {

    private String[] titles = {"Recording", "saved Recording"};

    public MyTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new RecordFragment();
            case 1:

                return new FileViewFragment();
        }


        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
