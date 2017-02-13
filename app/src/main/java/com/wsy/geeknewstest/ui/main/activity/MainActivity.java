package com.wsy.geeknewstest.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.BaseActivity;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.SearchEvent;
import com.wsy.geeknewstest.model.bean.VersionBean;
import com.wsy.geeknewstest.presenter.MainPresenter;
import com.wsy.geeknewstest.presenter.contract.MainContract;
import com.wsy.geeknewstest.ui.gank.fargment.GankMainFragment;
import com.wsy.geeknewstest.ui.gold.fragment.GoldMainFragment;
import com.wsy.geeknewstest.ui.main.fragment.AboutFragment;
import com.wsy.geeknewstest.ui.main.fragment.LikeFragment;
import com.wsy.geeknewstest.ui.main.fragment.SettingFragment;
import com.wsy.geeknewstest.ui.news.fragment.NewsMainFragment;
import com.wsy.geeknewstest.ui.video.activity.VideoActivity;
import com.wsy.geeknewstest.ui.wechat.fragment.WechatMainFragment;
import com.wsy.geeknewstest.ui.zhihu.fragment.ZhihuMainFragment;
import com.wsy.geeknewstest.util.SharedPreferenceUtil;
import com.wsy.geeknewstest.util.SnackbarUtil;
import com.wsy.geeknewstest.util.SystemUtil;

import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    @BindView(R.id.view_search)
    MaterialSearchView mSearchView;

    //DrawerLayout监听器
    ActionBarDrawerToggle mDrawerToggle;

    ZhihuMainFragment mZhihuFragment;
    WechatMainFragment mWechatFragment;
    GankMainFragment mGankFragment;
    GoldMainFragment mGoldFragment;
    NewsMainFragment mNewsFragment;
    VideoActivity mVideoActivity;
    LikeFragment mLikeFragment;
    SettingFragment mSettingFragment;
    AboutFragment mAboutFragment;

    MenuItem mLastMenuItem;
    MenuItem mSearchMenuItem;

    private int hideFragment = Constants.TYPE_ZHIHU;
    private int showFragment = Constants.TYPE_ZHIHU;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * 由于recreate 需要特殊处理夜间模式
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            SharedPreferenceUtil.setNightModeState(false);
        } else {
            showFragment = SharedPreferenceUtil.getCurrentItem();
            hideFragment = Constants.TYPE_ZHIHU;
            showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
            mNavigationView.getMenu().findItem(R.id.drawer_zhihu).setCheckable(false);
            mToolbar.setTitle(mNavigationView.getMenu().findItem(getCurrentItem(showFragment)).getTitle().toString());
            hideFragment = showFragment;
        }
    }

    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar, "知乎日报");

        mZhihuFragment = new ZhihuMainFragment();
        mWechatFragment = new WechatMainFragment();
        mGankFragment = new GankMainFragment();
        mGoldFragment = new GoldMainFragment();
        mNewsFragment = new NewsMainFragment();
        mLikeFragment = new LikeFragment();
        mSettingFragment = new SettingFragment();
        mAboutFragment = new AboutFragment();

        // 注册导航菜单抽屉的弹出和关闭事件
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        //初始化状态
        mDrawerToggle.syncState();
        //将DrawerLayout与DrawerToggle绑定
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //第一次进入设置为drawer_zhihu
        mLastMenuItem = mNavigationView.getMenu().findItem(R.id.drawer_zhihu);
        //加载多个根Fragment
        loadMultipleRootFragment(R.id.fl_main_content, 0, mZhihuFragment, mWechatFragment, mGankFragment, mLikeFragment, mSettingFragment, mAboutFragment, mGoldFragment, mNewsFragment);
        //设置NavigationItem点击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_zhihu:
                        showFragment = Constants.TYPE_ZHIHU;
                        mSearchMenuItem.setVisible(false);
                        break;

                    case R.id.drawer_wechat:
                        showFragment = Constants.TYPE_WECHAT;
                        mSearchMenuItem.setVisible(true);
                        break;

                    case R.id.drawer_gank:
                        showFragment = Constants.TYPE_GANK;
                        mSearchMenuItem.setVisible(true);
                        break;

                    case R.id.drawer_gold:
                        showFragment = Constants.TYPE_GOLD;
                        mSearchMenuItem.setVisible(false);
                        break;

                    case R.id.drawer_like:
                        showFragment = Constants.TYPE_LIKE;
                        mSearchMenuItem.setVisible(false);
                        break;

                    case R.id.drawer_setting:
                        showFragment = Constants.TYPE_SETTING;
                        mSearchMenuItem.setVisible(false);
                        break;

                    case R.id.drawer_about:
                        showFragment = Constants.TYPE_ABOUT;
                        mSearchMenuItem.setVisible(false);
                        break;

                    case R.id.drawer_video:
                        Intent intent = new Intent();
                        intent.setClass(mContext, VideoActivity.class);
                        mContext.startActivity(intent);
                        break;

                    case R.id.drawer_news:
                        showFragment = Constants.TYPE_NEWS;
                        mSearchMenuItem.setVisible(false);
                        break;

                }

                if (mLastMenuItem != null) {
                    mLastMenuItem.setChecked(false);
                }
                //储存当前所点击的item
                mLastMenuItem = item;
                SharedPreferenceUtil.setCurrentItem(showFragment);
                item.setCheckable(true);
                //设置标题
                mToolbar.setTitle(item.getTitle());
                //关闭抽屉
                mDrawerLayout.closeDrawers();
                //show一个Fragment,hide一个Fragment
                showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
                hideFragment = showFragment;
                return true;
            }
        });

        //设置搜索点击事件
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (showFragment == Constants.TYPE_GANK) {
                    mGankFragment.doSearch(query);
                } else if (showFragment == Constants.TYPE_WECHAT) {
                    RxBus.getDefault().post(new SearchEvent(query, Constants.TYPE_WECHAT));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //获取包信息,校验版本
        if (!SharedPreferenceUtil.getVersionPoint() && SystemUtil.isWifiConnected()) {
            SharedPreferenceUtil.setVersionPoint(true);
            try {
                PackageManager pm = getPackageManager();
                //使用权限获取包名
                PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                String versionName = pi.versionName;
                mPresenter.checkVersion(versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        mSearchView.setMenuItem(item);
        mSearchMenuItem = item;
        return true;
    }

    @Override
    public void showError(String msg) {
        SnackbarUtil.showShort(mToolbar, msg);
    }

    /**
     * 当点击返回键时，触发
     */
    @Override
    public void onBackPressedSupport() {
        if (mSearchView.isSearchOpen()) {  //搜索打开返回true
            mSearchView.closeSearch();
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出GeekNews吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getInstance().exitApp();
            }
        });
        builder.show();
    }

    @Override
    public void showUpdateDialog(VersionBean bean) {

    }

    /**
     * 申请权限的回调，处理申请权限成功失败
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 调用EasyPermissions的onRequestPermissionsResult方法，参数和系统方法保持一致，然后就不要关心具体的权限申请代码了
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 当申请的权限被同意之后会调用这个方法，requestCode为请求码，perms为申请同意的权限列表。
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    /**
     * 当申请的权限被拒绝之后会调用这个方法，requestCode为请求码，perms为被拒绝的权限列表。
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_ZHIHU:
                return mZhihuFragment;

            case Constants.TYPE_WECHAT:
                return mWechatFragment;

            case Constants.TYPE_GANK:
                return mGankFragment;

            case Constants.TYPE_GOLD:
                return mGoldFragment;

            case Constants.TYPE_NEWS:
                return mNewsFragment;

            case Constants.TYPE_LIKE:
                return mLikeFragment;

            case Constants.TYPE_SETTING:
                return mSettingFragment;

            case Constants.TYPE_ABOUT:
                return mAboutFragment;

        }
        return mZhihuFragment;
    }

    private int getCurrentItem(int item) {
        switch (item) {
            case Constants.TYPE_ZHIHU:
                return R.id.drawer_zhihu;

            case Constants.TYPE_WECHAT:
                return R.id.drawer_wechat;

            case Constants.TYPE_GANK:
                return R.id.drawer_gank;

            case Constants.TYPE_GOLD:
                return R.id.drawer_gold;

            case Constants.TYPE_NEWS:
                return R.id.drawer_news;

            case Constants.TYPE_LIKE:
                return R.id.drawer_like;

            case Constants.TYPE_SETTING:
                return R.id.drawer_setting;

            case Constants.TYPE_ABOUT:
                return R.id.drawer_about;

            case Constants.TYPE_VIDEO:
                return R.id.drawer_video;
        }
        return R.id.drawer_zhihu;
    }

}
