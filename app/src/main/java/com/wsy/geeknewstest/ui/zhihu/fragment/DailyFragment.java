package com.wsy.geeknewstest.ui.zhihu.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;

import com.wsy.geeknewstest.model.bean.zhihu.DailyBeforeListBean;
import com.wsy.geeknewstest.model.bean.zhihu.DailyListBean;
import com.wsy.geeknewstest.presenter.DailyPresenter;
import com.wsy.geeknewstest.presenter.contract.DailyContract;

import com.wsy.geeknewstest.ui.zhihu.activity.CalendarActivity;
import com.wsy.geeknewstest.ui.zhihu.activity.ZhihuDetailActivity;
import com.wsy.geeknewstest.ui.zhihu.adapter.DailyAdapter;
import com.wsy.geeknewstest.util.CircularAnimUtil;
import com.wsy.geeknewstest.util.DateUtil;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2016/11/20.
 * 日报
 */

public class DailyFragment extends BaseFragment<DailyPresenter> implements DailyContract.View {

    @BindView(R.id.fab_calender)
    FloatingActionButton fabCalender;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rv_daily_list)
    RecyclerView rvDailyList;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    String currentDate;
    DailyAdapter mAdapter;
    List<DailyListBean.StoriesBean> mList = new ArrayList<>();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initEventAndData() {
        currentDate = DateUtil.getTomorrowDate();
        mAdapter = new DailyAdapter(mContext, mList);
        mAdapter.setOnItemClickListener(new DailyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                //增加阅读记录
                mPresenter.insertReadToDB(mList.get(position).getId());
                mAdapter.setReadState(position, true);
                if (mAdapter.getIsBefore()) {  //是否为往日
                    mAdapter.notifyItemChanged(position + 1);
                } else {
                    mAdapter.notifyItemChanged(position + 2);
                }
                Intent intent = new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, "shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (currentDate.equals(DateUtil.getTomorrowDate())) {
                    mPresenter.getDailyData();
                } else {
                   /* int year = Integer.valueOf(currentDate.substring(0, 4));
                    int month = Integer.valueOf(currentDate.substring(4, 6));
                    int day = Integer.valueOf(currentDate.substring(6, 8));
                    CalendarDay date = CalendarDay.from(year, month - 1, day);
                    RxBus.getDefault().post(date);*/
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
        //为RecyclerView创建布局管理器，这里使用的是LinearLayoutManager，表示里面的Item排列是线性排列
        rvDailyList.setLayoutManager(new LinearLayoutManager(mContext));
        rvDailyList.setAdapter(mAdapter);
        ivProgress.start();
        mPresenter.getDailyData();
    }

    /**
     * 当天数据
     */
    @Override
    public void showContent(DailyListBean info) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mList = info.getStories();
        currentDate = String.valueOf(Integer.valueOf(info.getDate()) + 1);
        mAdapter.addDailyDate(info);
        mPresenter.startInterval();
        mPresenter.stopInterval();
    }

    /**
     * 过往数据
     */
    @Override
    public void showMoreContent(String date, DailyBeforeListBean info) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mPresenter.stopInterval();
        mList = info.getStories();
        currentDate = String.valueOf(Integer.valueOf(info.getDate()));
        mAdapter.addDailyBeforeDate(info);
    }

    @Override
    public void showProgress() {
        ivProgress.start();
    }

    @Override
    public void doInterval(int currentCount) {
        //制定初始化的页面
        mAdapter.changeTopPager(currentCount);
    }

    @OnClick(R.id.fab_calender)
    void startCalender() {
        Intent intent = new Intent();
        intent.setClass(mContext, CalendarActivity.class);
        CircularAnimUtil.startActivity(mActivity, intent, fabCalender, R.color.fab_bg);
    }

    @Override
    public void showError(String msg) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        SnackbarUtil.showShort(rvDailyList, msg);
    }
}
