package com.wsy.geeknewstest.ui.news.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleActivity;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.listener.PhotoDetailOnClickEvent;
import com.wsy.geeknewstest.model.bean.news.NewsPhotoDetail;
import com.wsy.geeknewstest.ui.news.adapter.PhotoPagerAdapter;
import com.wsy.geeknewstest.ui.news.fragment.PhotoDetailFragment;
import com.wsy.geeknewstest.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hasee on 2017/2/4.
 */

public class NewsPhotoDetailActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    PhotoViewPager mViewpager;
    @BindView(R.id.photo_detail_title_tv)
    TextView mPhotoDetailTitleTv;

    private NewsPhotoDetail mNewsPhotoDetail;
    private List<PhotoDetailFragment> mPhotoDetailFragmentList = new ArrayList<>();
    private PhotoPagerAdapter mAdapter;
    private Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getDefault().toObservable(PhotoDetailOnClickEvent.class)
                .subscribe(new Action1<PhotoDetailOnClickEvent>() {
                    @Override
                    public void call(PhotoDetailOnClickEvent photoDetailOnClickEvent) {
                        if (mPhotoDetailTitleTv.getVisibility() == View.VISIBLE) {
                            startAnimation(View.GONE, 0.9f, 0.5f);
                        } else {
                            mPhotoDetailTitleTv.setVisibility(View.VISIBLE);
                            startAnimation(View.VISIBLE, 0.5f, 0.9f);
                        }
                    }
                });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_news_photo_detail;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "图片新闻");
        mNewsPhotoDetail = getIntent().getParcelableExtra(Constants.IT_PHOTO_DETAIL);
        createFragment(mNewsPhotoDetail);
        initViewPager();
        setPhotoDetailTitle(0);
    }

    private void startAnimation(final int endState, float startValue, float endValue) {

        ObjectAnimator animator = ObjectAnimator
                .ofFloat(mPhotoDetailTitleTv, "alpha", startValue, endValue)  //透明度变化
                .setDuration(200);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPhotoDetailTitleTv.setVisibility(endState);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void createFragment(NewsPhotoDetail newsPhotoDetail) {
        mPhotoDetailFragmentList.clear();
        for (NewsPhotoDetail.Picture picture : newsPhotoDetail.getPictures()) {
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.IT_PHOTO_DETAIL_IMGSRC, picture.getImgSrc());
            fragment.setArguments(bundle);
            mPhotoDetailFragmentList.add(fragment);
        }
    }

    private void initViewPager() {
        mAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), mPhotoDetailFragmentList);
        mViewpager.setAdapter(mAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setPhotoDetailTitle(position);
            }
        });
    }

    public void setPhotoDetailTitle(int position) {
        String title = getTitle(position);
        mPhotoDetailTitleTv.setText(String.format("%1$d/%2$d　%3$s", position + 1, mPhotoDetailFragmentList.size(), title));
    }

    private String getTitle(int position) {
        String title = mNewsPhotoDetail.getPictures().get(position).getTitle();
        if (title == null) {
            title = mNewsPhotoDetail.getTitle();
        }
        return title;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }
}
