package com.wsy.geeknewstest.ui.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wsy.geeknewstest.ui.news.fragment.NewsPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/1/19.
 */

public class NewsFragmentMainAdapter extends FragmentPagerAdapter {

    private final List<String> mTitles;
    private List<NewsPagerFragment> mNewsFragmentList = new ArrayList<>();


    public NewsFragmentMainAdapter(FragmentManager fm, List<String> mTitles, List<NewsPagerFragment> mNewsFragmentList) {
        super(fm);
        this.mTitles = mTitles;
        this.mNewsFragmentList = mNewsFragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mNewsFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mNewsFragmentList.size();
    }
}
