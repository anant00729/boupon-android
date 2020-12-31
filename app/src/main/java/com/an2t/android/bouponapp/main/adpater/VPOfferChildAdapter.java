package com.an2t.android.bouponapp.main.adpater;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 8/23/17.
 */

public class VPOfferChildAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    public VPOfferChildAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
