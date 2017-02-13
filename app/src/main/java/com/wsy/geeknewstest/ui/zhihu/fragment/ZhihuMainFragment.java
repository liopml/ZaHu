package com.wsy.geeknewstest.ui.zhihu.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;


import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.SimpleFragment;
import com.wsy.geeknewstest.ui.zhihu.adapter.ZhihuMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/11/14.
 * 知乎主页
 */

public class ZhihuMainFragment extends SimpleFragment {

    @BindView(R.id.tab_zhihu_main)
    TabLayout mTabLayout;

    @BindView(R.id.vp_zhihu_main)
    ViewPager mViewPager;

    List<Fragment> fragments = new ArrayList<>();

    ZhihuMainAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zhihu_main;
    }

    @Override
    protected void initEventAndData() {
        fragments.add(new DailyFragment());
        fragments.add(new ThemeFragment());
        fragments.add(new SectionFragment());
        fragments.add(new HotFragment());
        mAdapter = new ZhihuMainAdapter(getChildFragmentManager(), fragments);
        //setOffscreenPageLimit方法用于设置ViewPager的后台加载页面个数。
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);

    }
}
