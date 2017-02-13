package com.wsy.geeknewstest.ui.gank.fargment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.gank.GankItemBean;
import com.wsy.geeknewstest.presenter.GirlPresenter;
import com.wsy.geeknewstest.presenter.contract.GirlContract;
import com.wsy.geeknewstest.ui.gank.activity.GirlDetailActivity;
import com.wsy.geeknewstest.ui.gank.adapter.GirlAdapter;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/12/7.
 */

public class GirlFragment extends BaseFragment<GirlPresenter> implements GirlContract.View {

    @BindView(R.id.rv_girl_content)
    RecyclerView rvGirlContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    private static final int SPAN_COUNT = 2;

    GirlAdapter mAdapter;
    List<GankItemBean> mList;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private boolean isLoadingMore = false;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girl;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new GirlAdapter(mContext, mList);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvGirlContent.setLayoutManager(mStaggeredGridLayoutManager);
        rvGirlContent.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getGirlData();
            }
        });
        rvGirlContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] visibleItems = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
                int lastItem = Math.max(visibleItems[0], visibleItems[1]);
                if (lastItem > mAdapter.getItemCount() - 5 && !isLoadingMore && dy > 0) {
                    isLoadingMore = true;
                    mPresenter.getMoreGirlData();
                }
            }
        });
        mAdapter.setOnItemClickListener(new GirlAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, GirlDetailActivity.class);
                intent.putExtra("url", mList.get(position).getUrl());
                intent.putExtra("id", mList.get(position).get_id());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, "shareView");
                mContext.startActivity(intent, options.toBundle());
            }
        });
        ivProgress.start();
        mPresenter.getGirlData();
    }

    @Override
    public void showContent(List<GankItemBean> list) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<GankItemBean> list) {
        isLoadingMore = false;
        ivProgress.stop();
        mList.addAll(list);
        //使用notifyDataSetChanged已加载的图片会有闪烁，遂使用inserted逐个插入
        for (int i = mList.size() - GirlPresenter.NUM_OF_PAGE; i < mList.size(); i++) {
            mAdapter.notifyItemInserted(i);
        }
    }
}
