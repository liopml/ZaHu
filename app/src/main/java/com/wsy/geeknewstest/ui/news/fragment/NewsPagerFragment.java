package com.wsy.geeknewstest.ui.news.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.model.bean.news.NewsPhotoDetail;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;
import com.wsy.geeknewstest.presenter.NewsPresenter;
import com.wsy.geeknewstest.presenter.contract.NewsContract;
import com.wsy.geeknewstest.ui.news.activity.NewsDetailActivity;
import com.wsy.geeknewstest.ui.news.activity.NewsPhotoDetailActivity;
import com.wsy.geeknewstest.ui.news.adapter.NewsListAdapter;
import com.wsy.geeknewstest.widget.ProgressImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2017/1/19.
 */

public class NewsPagerFragment extends BaseFragment<NewsPresenter> implements NewsContract.View, NewsListAdapter.OnItemClickListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;
    @BindView(R.id.rv_news_list)
    RecyclerView rvNewsList;

    private String mType;
    private String mId;

    private boolean isLoadingMore = false;

    private NewsListAdapter mNewsListAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initEventAndData() {
        mType = getArguments().getString(Constants.NEWS_TYPE);
        mId = getArguments().getString(Constants.NEWS_ID);
        //使RecyclerView保持固定的大小
        rvNewsList.setHasFixedSize(true);
        rvNewsList.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        rvNewsList.setItemAnimator(new DefaultItemAnimator());
        rvNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                if (!isLoadingMore && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1) {
                    isLoadingMore = true;
                    mPresenter.getMoreGoldData();
                }
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getGoldData(mType, mId);
            }
        });
        mNewsListAdapter = new NewsListAdapter(mContext, new ArrayList<NewsSummaryBean>());
        mNewsListAdapter.setOnItemClickListener(this);
        rvNewsList.setAdapter(mNewsListAdapter);
        ivProgress.start();
        mPresenter.getGoldData(mType, mId);
    }

    @Override
    public void showContent(List<NewsSummaryBean> newsSummaryBeanList) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        } else {
            ivProgress.stop();
        }
        mNewsListAdapter.updateData(newsSummaryBeanList);
        mNewsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMoreContent(List<NewsSummaryBean> newsSummaryBeanMoreList) {
        mNewsListAdapter.addMore(newsSummaryBeanMoreList);
        isLoadingMore = false;
    }

    @Override
    public void onItemClick(View view, int position, boolean isPhoto) {
        if (isPhoto) {
            NewsPhotoDetail newsPhotoDetail = getPhotoDetail(position);
            goToPhotoDetailActivity(newsPhotoDetail);
        } else {
            goToNewsDetailActivity(view, position);
        }
    }

    private void goToNewsDetailActivity(View view, int position) {
        Intent intent = setIntent(position);
        startActivity(view, intent);
    }

    @NonNull
    private Intent setIntent(int position) {
        List<NewsSummaryBean> newsSummaryList = mNewsListAdapter.getList();
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra(Constants.IT_NEWS_POST_ID, newsSummaryList.get(position).getPostid());
        intent.putExtra(Constants.IT_NEWS_IMG_RES, newsSummaryList.get(position).getImgsrc());
        return intent;
    }

    private void startActivity(View view, Intent intent) {
        ImageView newsSummaryPhotoIv = (ImageView) view.findViewById(R.id.news_summary_photo_iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, newsSummaryPhotoIv, Constants.TRANSITION_ANIMATION_NEWS_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        }
    }

    private NewsPhotoDetail getPhotoDetail(int position) {
        NewsSummaryBean newsSummaryBean = mNewsListAdapter.getList().get(position);
        NewsPhotoDetail newsPhotoDetail = new NewsPhotoDetail();
        newsPhotoDetail.setTitle(newsSummaryBean.getTitle());
        setPictures(newsSummaryBean, newsPhotoDetail);
        return newsPhotoDetail;
    }

    private void setPictures(NewsSummaryBean newsSummaryBean, NewsPhotoDetail newsPhotoDetail) {

        List<NewsPhotoDetail.Picture> pictureList = new ArrayList<>();

        if (newsSummaryBean.getAds() != null) {
            for (NewsSummaryBean.AdsBean entity : newsSummaryBean.getAds()) {
                setValuesAndAddToList(pictureList, entity.getTitle(), entity.getImgsrc());
            }
        } else if (newsSummaryBean.getImgextra() != null) {
            for (NewsSummaryBean.ImgextraBean entity : newsSummaryBean.getImgextra()) {
                setValuesAndAddToList(pictureList, null, entity.getImgsrc());
            }
        } else {
            setValuesAndAddToList(pictureList, null, newsSummaryBean.getImgsrc());
        }

        newsPhotoDetail.setPictures(pictureList);
    }

    private void setValuesAndAddToList(List<NewsPhotoDetail.Picture> pictureList, String title, String imgSrc) {
        NewsPhotoDetail.Picture picture = new NewsPhotoDetail.Picture();

        if (title != null) {
            picture.setTitle(title);
        }
        picture.setImgSrc(imgSrc);

        pictureList.add(picture);
    }

    private void goToPhotoDetailActivity(NewsPhotoDetail newsPhotoDetail) {
        Intent intent = new Intent(getActivity(), NewsPhotoDetailActivity.class);
        intent.putExtra(Constants.IT_PHOTO_DETAIL, newsPhotoDetail);
        startActivity(intent);
    }
}
