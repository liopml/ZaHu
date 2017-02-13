package com.wsy.geeknewstest.ui.gold.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wsy.geeknewstest.ui.gold.fragment.GoldPagerFragment;

import java.util.List;

/**
 * Created by hasee on 2016/12/13.
 */

public class GoldPagerAdapter extends FragmentPagerAdapter {

    private List<GoldPagerFragment> fragments;

    public GoldPagerAdapter(FragmentManager fm, List<GoldPagerFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
