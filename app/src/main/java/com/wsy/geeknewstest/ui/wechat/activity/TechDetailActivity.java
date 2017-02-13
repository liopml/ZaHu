package com.wsy.geeknewstest.ui.wechat.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleActivity;
import com.wsy.geeknewstest.model.bean.RealmLikeBean;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.util.ShareUtil;
import com.wsy.geeknewstest.util.SharedPreferenceUtil;
import com.wsy.geeknewstest.util.SystemUtil;

import butterknife.BindView;

/**
 * Created by hasee on 2016/12/3.
 */

public class TechDetailActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.wv_tech_content)
    WebView wvTechContent;
    @BindView(R.id.tv_progress)
    TextView tvProgress;

    RealmHelper mRealmHelper;
    MenuItem menuItem;

    String title, url, imgUrl, id;
    int type;
    boolean isLiked;

    @Override
    protected int getLayout() {
        return R.layout.activity_tech_detail;
    }

    @Override
    protected void initEventAndData() {
        mRealmHelper = App.getAppComponent().realmHelper();
        Intent intent = getIntent();
        type = intent.getExtras().getInt(Constants.IT_DETAIL_TYPE);
        title = intent.getExtras().getString(Constants.IT_DETAIL_TITLE);
        url = intent.getExtras().getString(Constants.IT_DETAIL_URL);
        imgUrl = intent.getExtras().getString(Constants.IT_DETAIL_IMG_URL);
        id = intent.getExtras().getString(Constants.IT_DETAIL_ID);
        setToolBar(toolBar, title);
        //设置网路
        WebSettings settings = wvTechContent.getSettings();
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
        wvTechContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(url);
                return true;
            }
        });
        wvTechContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    tvProgress.setVisibility(View.GONE);
                } else {
                    tvProgress.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams lp = tvProgress.getLayoutParams();
                    lp.width = App.SCREEN_WIDTH * i / 100;
                }
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                setTitle(s);
            }
        });
        wvTechContent.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvTechContent.canGoBack()) {
            wvTechContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tech_meun, menu);
        menuItem = menu.findItem(R.id.action_like);
        boolean state = mRealmHelper.queryLikeId(id);
        setLikeState(state);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_like:
                if (isLiked) {
                    item.setIcon(R.mipmap.ic_toolbar_like_n);
                    mRealmHelper.deleteLikeBean(this.id);
                    isLiked = false;
                } else {
                    item.setIcon(R.mipmap.ic_toolbar_like_p);
                    RealmLikeBean bean = new RealmLikeBean();
                    bean.setId(this.id);
                    bean.setImage(url);
                    bean.setTitle(title);
                    bean.setType(Constants.TYPE_WECHAT);
                    bean.setTime(System.currentTimeMillis());
                    mRealmHelper.insertLikeBean(bean);
                    isLiked = true;
                }
                break;
            case R.id.action_copy:
                SystemUtil.copyToClipBoard(mContext, url);
                return true;
            case R.id.action_share:
                ShareUtil.shareText(mContext, url, "分享一篇文章");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLikeState(boolean state) {
        if (state) {
            menuItem.setIcon(R.mipmap.ic_toolbar_like_p);
            isLiked = true;
        } else {
            menuItem.setIcon(R.mipmap.ic_toolbar_like_n);
            isLiked = false;
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finishAfterTransition();
        }
    }

    public static class Builder {

        private String title;
        private String url;
        private String imgUrl;
        private String id;
        private int type;
        private View shareView;
        private Context mContext;
        private Activity mActivity;

        public Builder() {
        }

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setAnimConfig(Activity mActivity, View shareView) {
            this.mActivity = mActivity;
            this.shareView = shareView;
            return this;
        }
    }

    public static void launch(Builder builder) {
        if (builder.shareView != null) {
            Intent intent = new Intent();
            intent.setClass(builder.mContext, TechDetailActivity.class);
            intent.putExtra(Constants.IT_DETAIL_URL, builder.url);
            intent.putExtra(Constants.IT_DETAIL_IMG_URL, builder.imgUrl);
            intent.putExtra(Constants.IT_DETAIL_TITLE, builder.title);
            intent.putExtra(Constants.IT_DETAIL_ID, builder.id);
            intent.putExtra(Constants.IT_DETAIL_TYPE, builder.type);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(builder.mActivity, builder.shareView, "shareView");
            builder.mContext.startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent();
            intent.setClass(builder.mContext, TechDetailActivity.class);
            intent.putExtra(Constants.IT_DETAIL_URL, builder.url);
            intent.putExtra(Constants.IT_DETAIL_IMG_URL, builder.imgUrl);
            intent.putExtra(Constants.IT_DETAIL_TITLE, builder.title);
            intent.putExtra(Constants.IT_DETAIL_ID, builder.id);
            intent.putExtra(Constants.IT_DETAIL_TYPE, builder.type);
            builder.mContext.startActivity(intent);
        }
    }
}
