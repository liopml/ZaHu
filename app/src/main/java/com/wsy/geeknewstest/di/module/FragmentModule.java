package com.wsy.geeknewstest.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.wsy.geeknewstest.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hasee on 2016/11/20.
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
