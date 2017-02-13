package com.wsy.geeknewstest.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.news.NewsChannelTableBean;
import com.wsy.geeknewstest.model.bean.news.NewsManagerBean;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;
import com.wsy.geeknewstest.presenter.NewsMainPresenter;
import com.wsy.geeknewstest.presenter.contract.NewsMainContract;
import com.wsy.geeknewstest.ui.news.activity.NewsChannelActivity;
import com.wsy.geeknewstest.ui.news.adapter.NewsFragmentMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2016/12/22.
 */

public class NewsMainFragment extends BaseFragment<NewsMainPresenter> implements NewsMainContract.View {

    @BindView(R.id.tab_news_main)
    TabLayout mTabLayout;
    @BindView(R.id.vp_news_main)
    ViewPager mViewPager;

    List<NewsPagerFragment> fragments = new ArrayList<>();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_main;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.initManagerList();
    }

    @Override
    public void updateTab(List<NewsChannelTableBean> mList) {
        fragments.clear();
        mTabLayout.removeAllTabs();
        List<String> channelNames = new ArrayList<>();
        setNewsList(mList, channelNames);
        setViewPager(channelNames);
    }

    private void setNewsList(List<NewsChannelTableBean> mList, List<String> channelNames) {
        for (NewsChannelTableBean newsChannel : mList) {
            if (newsChannel.isNewsChannelSelect()){
                NewsPagerFragment newsPagerFragment = createListFragments(newsChannel);
                fragments.add(newsPagerFragment);
                channelNames.add(newsChannel.getNewsChannelName());
            }
        }
    }

    private NewsPagerFragment createListFragments(NewsChannelTableBean newsChannel) {
        NewsPagerFragment fragment = new NewsPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(Constants.NEWS_TYPE, newsChannel.getNewsChannelType());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        NewsFragmentMainAdapter adapter = new NewsFragmentMainAdapter(getChildFragmentManager(), channelNames, fragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void jumpToManager(NewsManagerBean mList) {
        Intent intent = new Intent(getActivity(), NewsChannelActivity.class);
        intent.putExtra(Constants.IT_NEWS_MANAGER, mList);
        mContext.startActivity(intent);
    }

    @OnClick(R.id.iv_news_menu)
    public void onClick(View view) {
        mPresenter.setManagerList();
    }

}
