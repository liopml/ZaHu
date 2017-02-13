package com.wsy.geeknewstest.ui.zhihu.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseActivity;
import com.wsy.geeknewstest.component.ImageLoader;
import com.wsy.geeknewstest.model.bean.zhihu.DetailExtraBean;
import com.wsy.geeknewstest.model.bean.zhihu.ZhihuDetailBean;
import com.wsy.geeknewstest.presenter.ZhihuDetailPresenter;
import com.wsy.geeknewstest.presenter.contract.ZhihuDetailContract;
import com.wsy.geeknewstest.util.HtmlUtil;
import com.wsy.geeknewstest.util.ShareUtil;
import com.wsy.geeknewstest.util.SharedPreferenceUtil;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.util.SystemUtil;
import com.wsy.geeknewstest.widget.ProgressImageView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2016/11/25.
 */

public class ZhihuDetailActivity extends BaseActivity<ZhihuDetailPresenter> implements ZhihuDetailContract.View {

    @BindView(R.id.detail_bar_image)
    ImageView detailBarImage;
    @BindView(R.id.detail_bar_copyright)
    TextView detailBarCopyright;
    @BindView(R.id.view_toolbar)
    Toolbar viewToolbar;
    @BindView(R.id.clp_toolbar)
    CollapsingToolbarLayout clpToolbar;
    @BindView(R.id.wv_detail_content)
    WebView wvDetailContent;
    @BindView(R.id.iv_progress)
    ProgressImageView ivProgress;
    @BindView(R.id.nsv_scroller)
    NestedScrollView nsvScroller;
    @BindView(R.id.tv_detail_bottom_like)
    TextView tvDetailBottomLike;
    @BindView(R.id.tv_detail_bottom_comment)
    TextView tvDetailBottomComment;
    @BindView(R.id.tv_detail_bottom_share)
    TextView tvDetailBottomShare;
    @BindView(R.id.ll_detail_bottom)
    FrameLayout llDetailBottom;
    @BindView(R.id.fab_like)
    FloatingActionButton fabLike;

    int id = 0;
    int allNum = 0;
    int shortNum = 0;
    int longNum = 0;

    String shareUrl;
    String imgUrl;

    boolean isBottomShow = true;
    boolean isImageShow = false;
    boolean isNotTransition = false;
    boolean isTransitionEnd = false;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_zhihu_detail;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(viewToolbar, "");
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        isNotTransition = intent.getBooleanExtra("isNotTransition", false);
        mPresenter.queryLikeData(id);
        mPresenter.getDetailData(id);
        mPresenter.getExtraData(id);
        ivProgress.start();
        //设置网络
        WebSettings settings = wvDetailContent.getSettings();
        if (SharedPreferenceUtil.getNoImageState()) {
            //设置WebView是否以http、https方式访问从网络加载图片资源，默认false
            settings.setBlockNetworkImage(true);
        }
        if (SharedPreferenceUtil.getAutoCacheState()) {
            //设置Application缓存API是否开启，默认false，设置有效的缓存路径参考setAppCachePath(String path)方法
            settings.setAppCacheEnabled(true);
            //设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
            settings.setDomStorageEnabled(true);
            //设置是否开启数据库存储API权限，默认false，未开启，可以参考setDatabasePath(String path)
            settings.setDatabaseEnabled(true);
        }
        if (SystemUtil.isNetworkConnected()) {
            //根据cache-control决定是否从网络上取数据。
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        //缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);
        //将图片调整到适合webview的大小
        //settings.setUseWideViewPort(true);
        //设置WebView底层的布局算法,将会重新生成WebView布局.SINGLE_COLUMN把所有内容放到WebView组件等宽的一列中
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放。
        settings.setSupportZoom(true);
        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        wvDetailContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        //监听滑动
        nsvScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0 && isBottomShow) {  //下移隐藏
                    isBottomShow = false;
                    llDetailBottom.animate().translationY(llDetailBottom.getHeight());
                } else if (scrollY - oldScrollY < 0 && !isBottomShow) {   //上移出现
                    isBottomShow = true;
                    llDetailBottom.animate().translationY(0);
                }
            }
        });

        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                /**
                 * 测试发现部分手机(如红米note2)上加载图片会变形,没有达到centerCrop效果
                 * 查阅资料发现Glide配合SharedElementTransition是有坑的,需要在Transition动画结束后再加载图片
                 * https://github.com/TWiStErRob/glide-support/blob/master/src/glide3/java/com/bumptech/glide/supportapp/github/_847_shared_transition/DetailFragment.java
                 */
                isTransitionEnd = true;
                if (imgUrl != null) {
                    isImageShow = true;
                    ImageLoader.load(mContext, imgUrl, detailBarImage);
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @Override
    public void showContent(ZhihuDetailBean zhihuDetailBean) {
        ivProgress.stop();
        imgUrl = zhihuDetailBean.getImage();
        shareUrl = zhihuDetailBean.getShare_url();
        if (isNotTransition) {
            ImageLoader.load(mContext, zhihuDetailBean.getImage(), detailBarImage);
        } else {
            if (!isImageShow && isTransitionEnd) {
                ImageLoader.load(mContext, zhihuDetailBean.getImage(), detailBarImage);
            }
        }
        clpToolbar.setTitle(zhihuDetailBean.getTitle());
        detailBarCopyright.setText(zhihuDetailBean.getImage_source());
        String htmlData = HtmlUtil.createHtmlData(zhihuDetailBean.getBody(), zhihuDetailBean.getCss(), zhihuDetailBean.getJs());
        wvDetailContent.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
    }


    @Override
    public void showExtraInfo(DetailExtraBean detailExtraBean) {
        ivProgress.stop();
        tvDetailBottomLike.setText(String.format("%d个赞", detailExtraBean.getPopularity()));
        tvDetailBottomComment.setText(String.format("%d条评论", detailExtraBean.getComments()));
        allNum = detailExtraBean.getComments();
        shortNum = detailExtraBean.getShort_comments();
        longNum = detailExtraBean.getLong_comments();
    }

    @Override
    public void setLikeButtonState(boolean isLiked) {
        fabLike.setSelected(isLiked);
    }

    @Override
    public void showError(String msg) {
        ivProgress.stop();
        SnackbarUtil.showShort(getWindow().getDecorView(), msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvDetailContent.canGoBack()) {
            wvDetailContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finishAfterTransition();
        }
    }

    @OnClick(R.id.tv_detail_bottom_comment)
    void gotoComment() {
        Intent intent = getIntent();
        intent.setClass(this, CommentActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("allNum", allNum);
        intent.putExtra("shortNum", shortNum);
        intent.putExtra("longNum", longNum);
        startActivity(intent);
    }

    @OnClick(R.id.tv_detail_bottom_share)
    void shareUrl() {
        ShareUtil.shareText(mContext, shareUrl, "分享一篇文章");
    }

    @OnClick(R.id.fab_like)
    void likeArticle() {
        if (fabLike.isSelected()) {
            fabLike.setSelected(false);
            mPresenter.deleteLikeData();
        } else {
            fabLike.setSelected(true);
            mPresenter.insertLikeData();
        }
    }

}
