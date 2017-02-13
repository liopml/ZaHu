package com.wsy.geeknewstest.util;

import com.orhanobut.logger.Logger;
import com.wsy.geeknewstest.BuildConfig;

/**
 * Created by hasee on 2016/11/6.
 */

public class LogUtil {

    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "com.codeest.geeknewstest";

    public static void e(String tag, Object o) {
        if (isDebug) {
            Logger.e(tag, o);
        }
    }

    public static void e(Object o) {
        LogUtil.e(TAG, o);
    }

    public static void w(String tag, Object o) {
        if (isDebug) {
            Logger.w(tag, o);
        }
    }

    public static void w(Object o) {
        LogUtil.w(TAG, o);
    }

    public static void d(String msg) {
        if (isDebug) {
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Logger.i(msg);
        }
    }
}
