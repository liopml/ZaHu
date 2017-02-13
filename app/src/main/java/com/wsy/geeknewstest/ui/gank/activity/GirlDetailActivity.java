package com.wsy.geeknewstest.ui.gank.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.SimpleActivity;
import com.wsy.geeknewstest.model.bean.RealmLikeBean;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.util.ShareUtil;
import com.wsy.geeknewstest.util.SystemUtil;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hasee on 2016/12/7.
 */

public class GirlDetailActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.iv_girl_detail)
    ImageView ivGirlDetail;

    Bitmap bitmap;
    RealmHelper mRealmHelper;
    PhotoViewAttacher mAttacher;
    MenuItem menuItem;

    String url;
    String id;

    boolean isLiked;

    @Override
    protected int getLayout() {
        return R.layout.activity_girl_detail;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(toolBar, "");
        mRealmHelper = App.getAppComponent().realmHelper();
        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        id = intent.getExtras().getString("id");
        if (url != null) {
            Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            bitmap = resource;
                            ivGirlDetail.setImageBitmap(resource);
                            mAttacher = new PhotoViewAttacher(ivGirlDetail);
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.girl_menu, menu);
        menuItem = menu.findItem(R.id.action_like);
        setLikeState(mRealmHelper.queryLikeId(id));
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_like:
                if (isLiked) {
                    item.setIcon(R.mipmap.ic_toolbar_like_n);
                    mRealmHelper.deleteLikeBean(this.id);
                } else {
                    item.setIcon(R.mipmap.ic_toolbar_like_p);
                    RealmLikeBean bean = new RealmLikeBean();
                    bean.setId(this.id);
                    bean.setImage(url);
                    bean.setType(Constants.TYPE_GIRL);
                    bean.setTime(System.currentTimeMillis());
                    mRealmHelper.insertLikeBean(bean);
                }
                break;
            case R.id.action_save:
                SystemUtil.saveBitmapToFile(mContext, url, bitmap, ivGirlDetail, false);
                break;
            case R.id.action_share:
                ShareUtil.shareImage(mContext, SystemUtil.saveBitmapToFile(mContext, url, bitmap, ivGirlDetail, true), "分享一只妹纸");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finishAfterTransition();
        }
    }
}
