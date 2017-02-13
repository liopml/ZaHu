package com.wsy.geeknewstest.ui.news.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleActivity;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.news.NewsChannelTableBean;
import com.wsy.geeknewstest.model.bean.news.NewsManagerBean;
import com.wsy.geeknewstest.ui.news.adapter.NewsChannelAdapter;
import com.wsy.geeknewstest.widget.ItemDragHelperCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by hasee on 2017/1/21.
 */

public class NewsChannelActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.news_channel_rv)
    RecyclerView mRecyclerView;

    RealmList<NewsChannelTableBean> mList;
    ItemDragHelperCallback mCallback;
    NewsChannelAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_news_channel;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(toolBar, "频道管理");
        mList = ((NewsManagerBean) getIntent().getParcelableExtra(Constants.IT_NEWS_MANAGER)).getManagerList();
        final List<NewsChannelTableBean> myItems = new ArrayList<>();
        List<NewsChannelTableBean> otherItems = new ArrayList<>();

        for (NewsChannelTableBean item : mList) {
            if (item.isNewsChannelSelect()) {
                myItems.add(item);
            } else {
                otherItems.add(item);
            }
        }

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);

        mCallback = new ItemDragHelperCallback(new ItemDragHelperCallback.OnItemMoveListener() {
            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                if (mList != null) {
                    Collections.swap(mList, fromPosition - 1, toPosition - 1);
                    mAdapter.notifyItemMoved(fromPosition, toPosition);
                    return true;
                }
                return false;
            }
        });
        final ItemTouchHelper helper = new ItemTouchHelper(mCallback);
        helper.attachToRecyclerView(mRecyclerView);

        mAdapter = new NewsChannelAdapter(this, helper, myItems, otherItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                return viewType == NewsChannelAdapter.TYPE_MY || viewType == NewsChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnMyChannelItemClickListener(new NewsChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().post(new NewsManagerBean(mList));
    }
}
