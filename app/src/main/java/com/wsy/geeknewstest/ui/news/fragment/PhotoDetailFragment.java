package com.wsy.geeknewstest.ui.news.fragment;

import android.view.View;
import android.widget.ProgressBar;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleFragment;
import com.wsy.geeknewstest.component.ImageLoader;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.listener.PhotoDetailOnClickEvent;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hasee on 2017/2/4.
 */

public class PhotoDetailFragment extends SimpleFragment {

    @BindView(R.id.photo_view)
    PhotoView mPhotoView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private String mImgSrc;
    private Subscription mSubscription;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_photo_detail;
    }

    @Override
    protected void initEventAndData() {
        if (getArguments() != null) {
            mImgSrc = getArguments().getString(Constants.IT_PHOTO_DETAIL_IMGSRC);
        }

        mProgressBar.setVisibility(View.VISIBLE);
        initPhotoView();
        setPhotoViewClickEvent();
    }

    private void initPhotoView() {
        mSubscription = Observable.timer(100, TimeUnit.MILLISECONDS)  // 直接使用glide加载的话，activity切换动画时背景短暂为默认背景色
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        ImageLoader.load(mContext, mImgSrc, mPhotoView);
                    }
                });
    }

    private void setPhotoViewClickEvent() {
        mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                handleOnTabEvent();
            }

            @Override
            public void onOutsidePhotoTap() {
                handleOnTabEvent();
            }
        });
    }

    private void handleOnTabEvent() {
        RxBus.getDefault().post(new PhotoDetailOnClickEvent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }
}
