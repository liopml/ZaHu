package com.wsy.geeknewstest.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by hasee on 2016/11/21.
 * 加载显示
 */

public class ProgressImageView extends ImageView {

    public ProgressImageView(Context context) {
        super(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        setVisibility(View.VISIBLE);
        Animatable animatable = (Animatable) getDrawable();
        if (!animatable.isRunning()) { //如果没有运行
            animatable.start();
        }
    }

    public void stop() {
        Animatable animatable = (Animatable) getDrawable();
        if (animatable.isRunning()) {
            animatable.stop();
        }
        setVisibility(View.GONE);
    }
}
