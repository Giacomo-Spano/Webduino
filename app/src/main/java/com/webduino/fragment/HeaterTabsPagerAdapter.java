package com.webduino.fragment;

/**
 * Created by giaco on 06/01/2018.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HeaterTabsPagerAdapter extends FragmentStatePagerAdapter {
    public HeaterTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return HeaterDetailsPageFragment.newInstance();
        else if (position == 1)
            return PageFragment.newInstance(position + 1);
        else if (position == 2)
            return PageFragment.newInstance(position + 1);
        else
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "TAB " + (position + 1);
    }
}