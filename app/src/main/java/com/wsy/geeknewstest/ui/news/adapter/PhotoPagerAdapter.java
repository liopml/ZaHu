package com.wsy.geeknewstest.ui.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wsy.geeknewstest.ui.news.fragment.PhotoDetailFragment;

import java.util.List;

/**
 * Created by hasee on 2017/2/4.
 */

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private List<PhotoDetailFragment> mFragmentList;

    public PhotoPagerAdapter(FragmentManager fm, List<PhotoDetailFragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
