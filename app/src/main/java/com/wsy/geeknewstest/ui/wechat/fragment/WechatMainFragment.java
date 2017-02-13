package com.wsy.geeknewstest.ui.wechat.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.weixin.WXItemBean;
import com.wsy.geeknewstest.presenter.WechatPresenter;
import com.wsy.geeknewstest.presenter.contract.WechatContract;
import com.wsy.geeknewstest.ui.wechat.adapter.WechatAdapter;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/12/1.
 * 微信精选
 */

public class WechatMainFragment extends BaseFragment<WechatPresenter> implements WechatContract.View {

    @BindView(R.id.rv_wechat_list)
    RecyclerView rvWechatList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    WechatAdapter mAdapter;
    List<WXItemBean> mList;

    boolean isLoadingMore = false;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wechat;
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        mAdapter = new WechatAdapter(mContext, mList);
        rvWechatList.setLayoutManager(new LinearLayoutManager(mContext));
        rvWechatList.setAdapter(mAdapter);
        rvWechatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取最后一个可见item的position
                int lastVisibleItem = ((LinearLayoutManager) rvWechatList.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                //item总数量
                int totalItemCount = rvWechatList.getLayoutManager().getItemCount();
                //还剩2个Item时加载更多
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    if (!isLoadingMore) {
                        isLoadingMore = true;
                        mPresenter.getMoreWechatData();
                    }
                }
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getWechatData();
            }
        });
        ivProgress.start();
        mPresenter.getWechatData();
    }

    @Override
    public void showContent(List<WXItemBean> list) {
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
    public void showMoreContent(List<WXItemBean> mList) {
        ivProgress.stop();
        mList.addAll(mList);
        mAdapter.notifyDataSetChanged();
        isLoadingMore = false;
    }

    @Override
    public void showError(String msg) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        SnackbarUtil.showShort(rvWechatList, msg);
    }
}
