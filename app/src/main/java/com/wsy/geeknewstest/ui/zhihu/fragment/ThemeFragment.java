package com.wsy.geeknewstest.ui.zhihu.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.zhihu.ThemeListBean;
import com.wsy.geeknewstest.presenter.ThemePresenter;
import com.wsy.geeknewstest.presenter.contract.ThemeContract;
import com.wsy.geeknewstest.ui.zhihu.activity.ThemeActivity;
import com.wsy.geeknewstest.ui.zhihu.adapter.ThemeAdapter;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/11/23.
 * 主题
 */

public class ThemeFragment extends BaseFragment<ThemePresenter> implements ThemeContract.View {

    @BindView(R.id.rv_theme_list)
    RecyclerView rvThemeList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    ThemeAdapter mAdapter;
    List<ThemeListBean.OthersBean> mList = new ArrayList<>();

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_theme;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new ThemeAdapter(mContext, mList);
        rvThemeList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvThemeList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ThemeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent intent = new Intent();
                intent.setClass(mContext, ThemeActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getThemeData();
            }
        });
        mPresenter.getThemeData();
        ivProgress.start();
    }

    @Override
    public void showContent(ThemeListBean themeListBean) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mList.clear();
        mList.addAll(themeListBean.getOthers());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        SnackbarUtil.showShort(rvThemeList, msg);
    }
}
