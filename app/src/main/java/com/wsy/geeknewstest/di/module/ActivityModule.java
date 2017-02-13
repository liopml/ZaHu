package com.wsy.geeknewstest.di.module;

import android.app.Activity;

import com.wsy.geeknewstest.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hasee on 2016/11/8.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }

}
