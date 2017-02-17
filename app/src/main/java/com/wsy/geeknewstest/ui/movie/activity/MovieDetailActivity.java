package com.wsy.geeknewstest.ui.movie.activity;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.base.BaseActivity;
import com.wsy.geeknewstest.model.bean.movie.SubjectsBean;
import com.wsy.geeknewstest.util.StatusBarUtil;
import com.wsy.geeknewstest.util.StatusBarUtils;
import com.wsy.geeknewstest.widget.CustomChangeBounds;
import com.wsy.geeknewstest.widget.MyNestedScrollView;

import java.lang.reflect.Method;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by hasee on 2017/2/16.
 */

public class MovieDetailActivity extends BaseActivity {

    @BindView(R.id.iv_one_photo)
    ImageView moviePhoto;
    @BindView(R.id.ll_progress_bar)
    LinearLayout llProgressBar;
    @BindView(R.id.ll_error_refresh)
    View refresh;
    @BindView(R.id.img_item_bg)
    ImageView ItemBg;
    @BindView(R.id.tb_base_title)
    Toolbar toolbar;
    @BindView(R.id.iv_base_titlebar_bg)
    ImageView titleBarBg;
    @BindView(R.id.mns_base)
    MyNestedScrollView scrollView;
    @BindView(R.id.img_progress)
    ImageView progress;

    // 标题
    private View TitleView;
    // 内容布局头部
    private View HeaderView;
    // 内容布局view
    private View ContentView;

    private SubjectsBean subjectsBean;
    private String mMoreUrl;
    private String mMovieName;

    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 滑动多少距离后标题不透明
    private int slidingDistance;

    private AnimationDrawable mAnimationDrawable;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //根布局
        View ll = getLayoutInflater().inflate(R.layout.activity_header_base, null);

        // 内容
        ContentView = getLayoutInflater().inflate(layoutResID, null, false);
        // 头部
        HeaderView = getLayoutInflater().inflate(R.layout.header_slider_shape, null, false);
        // 标题
        TitleView = getLayoutInflater().inflate(R.layout.base_header_title_bar, null, false);

        // title
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TitleView.setLayoutParams(titleParams);
        RelativeLayout mTitleContainer = (RelativeLayout) ll.findViewById(R.id.title_container);
        mTitleContainer.addView(TitleView);
        getWindow().setContentView(ll);

        // header
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        HeaderView.setLayoutParams(headerParams);
        RelativeLayout mHeaderContainer = (RelativeLayout) ll.findViewById(R.id.header_container);
        mHeaderContainer.addView(HeaderView);
        getWindow().setContentView(ll);

        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ContentView.setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) ll.findViewById(R.id.container);
        mContainer.addView(ContentView);
        getWindow().setContentView(ll);

        // 设置自定义元素共享切换动画
        setMotion(moviePhoto, true);

        // 初始化滑动渐变
        initSlideShapeTheme(setHeaderImgUrl(), ItemBg);

        // 设置toolbar
        setToolBars();

        // 加载动画
        mAnimationDrawable = (AnimationDrawable) progress.getDrawable();
        // 默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        // 点击加载失败布局
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        ContentView.setVisibility(View.GONE);
    }

    @Override
    protected void initEventAndData() {
        if (getIntent() != null) {
            subjectsBean = getIntent().getParcelableExtra("bean");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_header_menu, menu);
        return true;
    }

    /**
     * 显示popu内的图片
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    /**
     * 设置自定义 Shared Element切换动画
     * 默认不开启曲线路径切换动画，
     * 开启需要重写setHeaderPicView()，和调用此方法并将isShow值设为true
     *
     * @param imageView 共享的图片
     * @param isShow    是否显示曲线动画
     */
    private void setMotion(ImageView imageView, boolean isShow) {
        if (!isShow) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();
            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(imageView);

            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }

    /**
     * *** 初始化滑动渐变 一定要实现 ******
     *
     * @param imgUrl    header头部的高斯背景imageUrl
     * @param mHeaderBg header头部高斯背景ImageView控件
     */
    private void initSlideShapeTheme(String imgUrl, ImageView mHeaderBg) {
        setImgHeaderBg(imgUrl);

        // Toolbar的高度
        int toolbarHeight = toolbar.getLayoutParams().height;
        // Toolbar+状态栏的高度　
        int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最底端，保留Toolbar+状态栏的高度
        ViewGroup.LayoutParams params = titleBarBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) titleBarBg.getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);

        titleBarBg.setImageAlpha(0);
        StatusBarUtils.setTranslucentImageHeader(this, 0, toolbar);

        // 上移背景图片，使空白状态栏消失(这样下方就空了状态栏的高度)
        if (mHeaderBg != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHeaderBg.getLayoutParams();
            layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0);

            ViewGroup.LayoutParams imgItemBgparams = mHeaderBg.getLayoutParams();
            // 获得高斯图背景的高度
            imageBgHeight = imgItemBgparams.height;
        }

        // 变色
        initScrollViewListener();
        initNewSlidingParams();
    }

    /**
     * 加载titlebar背景
     */
    private void setImgHeaderBg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {

            // 高斯模糊背景 原来 参数：12,5  23,4
            Glide.with(this).load(imgUrl)
                    .error(R.mipmap.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this, 23, 4))  //设置高斯模糊
                    .listener(new RequestListener<String, GlideDrawable>() {  //监听加载状态
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // Toolbar背景设为透明
                            toolbar.setBackgroundColor(Color.TRANSPARENT);
                            // 背景图初始化为全透明
                            titleBarBg.setImageAlpha(0);
                            titleBarBg.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(titleBarBg);
        }
    }

    private String setHeaderImgUrl() {
        if (subjectsBean == null) {
            return "";
        }
        return subjectsBean.getImages().getMedium();
    }

    private void initScrollViewListener() {
        // 为了兼容23以下
        scrollView.setOnScrollChangeListener(new MyNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });
    }

    private void initNewSlidingParams() {
        int titleBarAndStatusHeight = (int) (App.getInstance().getResources().getDimension(R.dimen.nav_bar_height) + StatusBarUtil.getStatusBarHeight(this));
        // 减掉后，没到顶部就不透明了
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (App.getInstance().getResources().getDimension(R.dimen.base_header_activity_slide_more));
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);

        Drawable drawable = titleBarBg.getDrawable();

        if (drawable == null) {
            return;
        }
        if (scrolledY <= slidingDistance) {
            // title部分的渐变
            drawable.mutate().setAlpha((int) (alpha * 255));
            titleBarBg.setImageDrawable(drawable);
        } else {
            drawable.mutate().setAlpha(255);
            titleBarBg.setImageDrawable(drawable);
        }
    }

    private void setToolBars() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        // 手动设置才有效果
        toolbar.setTitleTextAppearance(this, R.style.ToolBar_Title);
        toolbar.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
        toolbar.inflateMenu(R.menu.base_header_menu);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.actionbar_more));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionbar_more:// 更多信息
                        //.....
                        break;
                }
                return false;
            }
        });
    }

    protected void showLoading() {
        if (llProgressBar.getVisibility() != View.VISIBLE) {
            llProgressBar.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (ContentView.getVisibility() != View.GONE) {
            ContentView.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
    }

    /**
     * 失败后点击刷新
     */
    protected void onRefresh() {
        loadMovieDetail();
    }

    private void loadMovieDetail() {

    }

    @Override
    public void showError(String msg) {

    }
}
