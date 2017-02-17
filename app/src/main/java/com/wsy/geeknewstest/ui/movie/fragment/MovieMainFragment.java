package com.wsy.geeknewstest.ui.movie.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.movie.MovieBean;
import com.wsy.geeknewstest.model.bean.movie.SubjectsBean;
import com.wsy.geeknewstest.presenter.MoviePresenter;
import com.wsy.geeknewstest.presenter.contract.MovieContract;
import com.wsy.geeknewstest.ui.movie.adapter.MovieAdapter;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by hasee on 2017/2/14.
 */

public class MovieMainFragment extends BaseFragment<MoviePresenter> implements MovieContract.View {

    @BindView(R.id.rv_movie_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;

    private MovieAdapter mAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_movie;
    }

    @Override
    protected void initEventAndData() {
        mAdapter = new MovieAdapter(mContext, new ArrayList<SubjectsBean>(),mActivity);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 需加，不然滑动不流畅
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
        ivProgress.start();
        mPresenter.getMovieData();
    }

    @Override
    public void showContent(MovieBean movieData) {
        ivProgress.stop();
        mAdapter.updateData(movieData.getSubjects());
        mAdapter.notifyDataSetChanged();
    }


}
