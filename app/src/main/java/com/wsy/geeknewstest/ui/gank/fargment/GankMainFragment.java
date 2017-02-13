package com.wsy.geeknewstest.ui.gank.fargment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleFragment;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.SearchEvent;
import com.wsy.geeknewstest.presenter.TechPresenter;
import com.wsy.geeknewstest.ui.gank.adapter.GankMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/12/4.
 */

public class GankMainFragment extends SimpleFragment {

    @BindView(R.id.tab_gank_main)
    TabLayout mTabLayout;
    @BindView(R.id.vp_gank_main)
    ViewPager mViewPager;

    List<Fragment> fragments = new ArrayList<>();

    GankMainAdapter mAdapter;

    TechFragment androidFragment;
    TechFragment iOSFragment;
    TechFragment webFragment;
    GirlFragment girlFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank_main;
    }

    @Override
    protected void initEventAndData() {
        androidFragment = new TechFragment();
        iOSFragment = new TechFragment();
        webFragment = new TechFragment();
        girlFragment = new GirlFragment();

        Bundle androidBundle = new Bundle();
        androidBundle.putString(Constants.IT_GANK_TYPE, GankMainAdapter.tabTitle[0]);
        androidBundle.putInt(Constants.IT_GANK_TYPE_CODE, Constants.TYPE_ANDROID);
        androidFragment.setArguments(androidBundle);

        Bundle iosBundle = new Bundle();
        iosBundle.putString(Constants.IT_GANK_TYPE, GankMainAdapter.tabTitle[1]);
        iosBundle.putInt(Constants.IT_GANK_TYPE_CODE, Constants.TYPE_IOS);
        iOSFragment.setArguments(iosBundle);

        Bundle webBundle = new Bundle();
        webBundle.putString(Constants.IT_GANK_TYPE, GankMainAdapter.tabTitle[2]);
        webBundle.putInt(Constants.IT_GANK_TYPE_CODE, Constants.TYPE_WEB);
        webFragment.setArguments(webBundle);

        fragments.add(androidFragment);
        fragments.add(iOSFragment);
        fragments.add(webFragment);
        fragments.add(girlFragment);
        mAdapter = new GankMainAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        //setOffscreenPageLimit方法用于设置ViewPager的后台加载页面个数。
        mViewPager.setOffscreenPageLimit(4);
        //将TabLayout和ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void doSearch(String query) {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_ANDROID));
                break;
            case 1:
                RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_IOS));
                break;
            case 2:
                RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_WEB));
                break;
            case 3:
                RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_GIRL));
                break;
        }
    }
}
