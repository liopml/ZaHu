package com.wsy.geeknewstest.ui.news.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.BaseActivity;
import com.wsy.geeknewstest.component.ImageLoader;
import com.wsy.geeknewstest.model.bean.news.NewsDetailBean;
import com.wsy.geeknewstest.presenter.NewsDetailPresenter;
import com.wsy.geeknewstest.presenter.contract.NewsDetailContract;
import com.wsy.geeknewstest.util.DateUtil;
import com.wsy.geeknewstest.util.LogUtil;
import com.wsy.geeknewstest.util.RxUtil;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.widget.URLImageGetter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hasee on 2017/2/5.
 */

public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailContract.View {

    @BindView(R.id.news_detail_photo_iv)
    ImageView mNewsDetailPhotoIv;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.news_detail_from_tv)
    TextView mNewsDetailFromTv;
    @BindView(R.id.news_detail_body_tv)
    TextView mNewsDetailBodyTv;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.mask_view)
    View mMaskView;

    private String mPostId;
    private String mNewsTitle;
    private String mShareLink;

    private URLImageGetter mUrlImageGetter;
    private Subscription mSubscription;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initEventAndData() {
        mPostId = getIntent().getStringExtra(Constants.IT_NEWS_POST_ID);
        mPresenter.getNewsDetailData(mPostId);
    }


    @Override
    public void showContent(NewsDetailBean newsDetailBean) {
        mShareLink = newsDetailBean.getShareLink();
        mNewsTitle = newsDetailBean.getTitle();
        String newsSource = newsDetailBean.getSource();
        String newsTime = DateUtil.formatDate(newsDetailBean.getPtime());
        String newsBody = newsDetailBean.getBody();
        String NewsImgSrc = getImgSrcs(newsDetailBean);

        setToolBar(mToolbar, mNewsTitle);
        mNewsDetailFromTv.setText(String.format("%1$s %2$s", newsSource, newsTime));
        ImageLoader.load(mContext, NewsImgSrc, mNewsDetailPhotoIv);
        setNewsDetailBodyTv(newsDetailBean, newsBody);
    }

    @Override
    public void showError(String msg) {
        mProgressBar.setVisibility(View.GONE);
        SnackbarUtil.show(mAppBar, msg);
    }

    @Override
    protected void onDestroy() {
        cancelUrlImageGetterSubscription();
        mSubscription.unsubscribe();
        super.onDestroy();
    }

    private String getImgSrcs(NewsDetailBean newsDetail) {
        List<NewsDetailBean.ImgBean> imgSrcs = newsDetail.getImg();
        String imgSrc;
        if (imgSrcs != null && imgSrcs.size() > 0) {
            imgSrc = imgSrcs.get(0).getSrc();
        } else {
            imgSrc = getIntent().getStringExtra(Constants.IT_NEWS_IMG_RES);
        }
        return imgSrc;
    }

    private void setNewsDetailBodyTv(final NewsDetailBean newsDetail, final String newsBody) {
        mSubscription = Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RollIn).playOn(mFab);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setBody(newsDetail, newsBody);
                    }
                });
    }

    private void setBody(NewsDetailBean newsDetail, String newsBody) {
        int imgTotal = newsDetail.getImg().size();
        if (imgTotal >= 2 && newsBody != null) {
//          mNewsDetailBodyTv.setMovementMethod(LinkMovementMethod.getInstance());//加这句才能让里面的超链接生效,实测经常卡机崩溃
            mUrlImageGetter = new URLImageGetter(mNewsDetailBodyTv, newsBody, imgTotal);
            mNewsDetailBodyTv.setText(Html.fromHtml(newsBody, mUrlImageGetter, null));
        } else {
            mNewsDetailBodyTv.setText(Html.fromHtml(newsBody));
        }
    }

    private void cancelUrlImageGetterSubscription() {
        try {
            if (mUrlImageGetter != null && mUrlImageGetter.mSubscription != null && !mUrlImageGetter.mSubscription.isUnsubscribed()) {
                mUrlImageGetter.mSubscription.unsubscribe();
                LogUtil.d("UrlImageGetter unsubscribe");
            }
        } catch (Exception e) {
            LogUtil.e("取消UrlImageGetter Subscription 出现异常： " + e.toString());
        }

    }

    @OnClick(R.id.fab)
    public void onClick() {
        share();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, getShareContents());
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @NonNull
    private String getShareContents() {
        if (mShareLink == null) {
            mShareLink = "";
        }
        return String.format("来自「News」的分享：%1$s %2$s", mNewsTitle, mShareLink);
    }


}
