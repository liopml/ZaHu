package com.wsy.geeknewstest.ui.zhihu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.zhihu.CommentBean;
import com.wsy.geeknewstest.presenter.CommentPresenter;
import com.wsy.geeknewstest.presenter.contract.CommentContract;
import com.wsy.geeknewstest.ui.zhihu.adapter.CommentAdapter;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/11/27.
 */

public class CommentFragment extends BaseFragment<CommentPresenter> implements CommentContract.View {

    @BindView(R.id.rv_comment_list)
    RecyclerView rvCommentList;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    CommentAdapter mAdapter;
    List<CommentBean.CommentsBean> mList;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initEventAndData() {
        Bundle bundle = getArguments();
        mPresenter.getCommentData(bundle.getInt("id"), bundle.getInt("kind"));
        ivProgress.start();
        rvCommentList.setVisibility(View.INVISIBLE);
        mList = new ArrayList<>();
        mAdapter = new CommentAdapter(mContext, mList);
        rvCommentList.setLayoutManager(new LinearLayoutManager(mContext));
        rvCommentList.setAdapter(mAdapter);
    }

    @Override
    public void showContent(CommentBean commentBean) {
        ivProgress.stop();
        rvCommentList.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(commentBean.getComments());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        ivProgress.stop();
        rvCommentList.setVisibility(View.VISIBLE);
    }
}
