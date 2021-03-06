package com.wsy.geeknewstest.ui.zhihu.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.zhihu.SectionListBean;
import com.wsy.geeknewstest.presenter.SectionPresenter;
import com.wsy.geeknewstest.presenter.contract.SectionContract;
import com.wsy.geeknewstest.ui.zhihu.adapter.SectionAdapter;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/11/23.
 * 专栏
 */

public class SectionFragment extends BaseFragment<SectionPresenter> implements SectionContract.View {

    @BindView(R.id.rv_section_list)
    RecyclerView rvSectionList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    List<SectionListBean.DataBean> mList;
    SectionAdapter mAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_section;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new SectionAdapter(mContext, mList);
        rvSectionList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvSectionList.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSectionData();
            }
        });
        mPresenter.getSectionData();
        ivProgress.start();
    }

    @Override
    public void showContent(SectionListBean sectionListBean) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mList.clear();
        mList.addAll(sectionListBean.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        SnackbarUtil.showShort(rvSectionList, msg);
    }
}
