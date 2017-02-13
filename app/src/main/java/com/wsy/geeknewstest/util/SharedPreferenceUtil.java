package com.wsy.geeknewstest.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.app.Constants;

/**
 * Created by hasee on 2016/11/13.
 */

public class SharedPreferenceUtil {

    private static final boolean DEFAULT_NIGHT_MODE = false;
    private static final boolean DEFAULT_NO_IMAGE = false;
    private static final boolean DEFAULT_AUTO_SAVE = true;
    private static final boolean DEFAULT_LIKE_POINT = false;
    private static final boolean DEFAULT_VERSION_POINT = false;
    private static final boolean DEFAULT_MANAGER_POINT = false;
    private static final boolean DEFAULT_NEWS_MANAGER_POINT = false;

    private static final int DEFAULT_CURRENT_ITEM = Constants.TYPE_ZHIHU;

    private static final String SHAREDPREFERENCES_NAME = "my_sp";

    public static SharedPreferences getAppSp() {  //获得SharedPreferences对象
        return App.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setNightModeState(boolean state) { //设置夜间模式
        getAppSp().edit().putBoolean(Constants.SP_NIGHT_MODE, state).apply();
    }

    public static boolean getNightModeState() {  //获得夜间模式
        return getAppSp().getBoolean(Constants.SP_NIGHT_MODE, DEFAULT_NIGHT_MODE);
    }

    public static void setNoImageState(boolean state) {  //设置是否没有图片
        getAppSp().edit().putBoolean(Constants.SP_NO_IMAGE, state).apply();
    }

    public static boolean getNoImageState() {  //获得没有设置图片的判断
        return getAppSp().getBoolean(Constants.SP_NO_IMAGE, DEFAULT_NO_IMAGE);
    }

    public static void setCurrentItem(int item) {  //设置当前项目
        getAppSp().edit().putInt(Constants.SP_CURRENT_ITEM, item).apply();
    }

    public static int getCurrentItem() {  //获取当前项目
        return getAppSp().getInt(Constants.SP_CURRENT_ITEM, DEFAULT_CURRENT_ITEM);
    }

    public static void setVersionPoint(boolean isFirst) {
        getAppSp().edit().putBoolean(Constants.SP_VERSION_POINT, isFirst).apply();
    }

    public static boolean getVersionPoint() {
        return getAppSp().getBoolean(Constants.SP_VERSION_POINT, DEFAULT_VERSION_POINT);
    }

    public static void setAutoCacheState(boolean state) {  //设置缓存
        getAppSp().edit().putBoolean(Constants.SP_AUTO_CACHE, state).apply();
    }

    public static boolean getAutoCacheState() {  //获取缓存
        return getAppSp().getBoolean(Constants.SP_AUTO_CACHE, DEFAULT_AUTO_SAVE);
    }

    public static void setLikePoint(boolean isFirst) {
        getAppSp().edit().putBoolean(Constants.SP_LIKE_POINT, isFirst).apply();
    }

    public static boolean getLikePoint() {
        return getAppSp().getBoolean(Constants.SP_LIKE_POINT, DEFAULT_LIKE_POINT);
    }

    public static void setManagerPoint(boolean isFirst) {
        getAppSp().edit().putBoolean(Constants.SP_MANAGER_POINT, isFirst).apply();
    }

    public static boolean getManagerPoint() {
        return getAppSp().getBoolean(Constants.SP_MANAGER_POINT, DEFAULT_MANAGER_POINT);
    }

    public static void setNewsManagerPoint(boolean isFirst) {
        getAppSp().edit().putBoolean(Constants.SP_NEWS_MANAGER_POINT, isFirst).apply();
    }

    public static boolean getNewsManagerPoint() {
        return getAppSp().getBoolean(Constants.SP_NEWS_MANAGER_POINT, DEFAULT_NEWS_MANAGER_POINT);
    }

}
