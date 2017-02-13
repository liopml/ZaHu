package com.wsy.geeknewstest.ui.gold.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleActivity;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.gold.GoldManagerBean;
import com.wsy.geeknewstest.model.bean.gold.GoldManagerItemBean;
import com.wsy.geeknewstest.ui.gold.adapter.GoldManagerAdapter;
import com.wsy.geeknewstest.widget.DefaultItemTouchHelpCallback;

import java.util.Collection;
import java.util.Collections;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by hasee on 2016/12/13.
 */

public class GoldManagerActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_gold_manager_list)
    RecyclerView rvGoldManagerList;

    RealmList<GoldManagerItemBean> mList;
    GoldManagerAdapter mAdapter;
    DefaultItemTouchHelpCallback mCallback;

    @Override
    protected int getLayout() {
        return R.layout.activity_gold_manager;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(toolBar, "首页特别展示");
        mList = ((GoldManagerBean) getIntent().getParcelableExtra(Constants.IT_GOLD_MANAGER)).getManagerList();
        mAdapter = new GoldManagerAdapter(mContext, mList);
        rvGoldManagerList.setLayoutManager(new LinearLayoutManager(mContext));
        rvGoldManagerList.setAdapter(mAdapter);
        mCallback = new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {

            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mList != null) {
                    Collections.swap(mList, srcPosition, targetPosition);
                    mAdapter.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
                return false;
            }
        });
        mCallback.setDragEnable(true);
        mCallback.setSwipeEnable(false);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(rvGoldManagerList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().post(new GoldManagerBean(mList));
    }
}
