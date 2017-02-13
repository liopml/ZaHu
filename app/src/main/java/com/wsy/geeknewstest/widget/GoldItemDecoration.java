package com.wsy.geeknewstest.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.util.SystemUtil;

/**
 * Created by hasee on 2016/12/12.
 */

public class GoldItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 指定item之间的间距(就是指定分割线的宽度)   回调顺序 1
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position > -1) {
            if (position == 0) {
                outRect.set(0, SystemUtil.dp2px(App.getInstance(), 15), 0, 0);
            } else if (position == 3) {
                outRect.set(0, SystemUtil.dp2px(App.getInstance(), 0.5f), 0, SystemUtil.dp2px(App.getInstance(), 15));
            } else {
                outRect.set(0, SystemUtil.dp2px(App.getInstance(), 0.5f), 0, 0);
            }
        }
    }
}
