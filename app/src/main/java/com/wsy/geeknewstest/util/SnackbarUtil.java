package com.wsy.geeknewstest.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by hasee on 2016/11/19.
 */

public class SnackbarUtil {

    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
