package com.wsy.geeknewstest.di.component;

import android.app.Activity;


import com.wsy.geeknewstest.di.ActivityScope;
import com.wsy.geeknewstest.di.component.AppComponent;
import com.wsy.geeknewstest.di.module.ActivityModule;
import com.wsy.geeknewstest.ui.main.activity.MainActivity;
import com.wsy.geeknewstest.ui.main.activity.WelcomeActivity;
import com.wsy.geeknewstest.ui.news.activity.NewsDetailActivity;
import com.wsy.geeknewstest.ui.video.activity.VideoActivity;
import com.wsy.geeknewstest.ui.zhihu.activity.SectionActivity;
import com.wsy.geeknewstest.ui.zhihu.activity.ThemeActivity;
import com.wsy.geeknewstest.ui.zhihu.activity.ZhihuDetailActivity;

import dagger.Component;

/**
 * Created by hasee on 2016/11/8.
 * 用@Component表示这个接口是一个连接器，能用@Component注解的只能是interface或者抽象类
 */
@ActivityScope
//这里表示Component会从ActivityModule类中拿那些用@Provides注解的方法来生成需要注入的实例
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    /**
     * 需要用到这个连接器的对象，就是这个对象里面有需要注入的属性（被标记为@Inject的属性）
     * 这里inject表示注入的意思，这个方法名可以随意更改，但建议就用inject即可。
     */
    void inject(WelcomeActivity welcomeActivity);

    void inject(MainActivity mainActivity);

    void inject(ZhihuDetailActivity zhihuDetailActivity);

    void inject(ThemeActivity themeActivity);

    void inject(SectionActivity sectionActivity);

    void inject(VideoActivity videoActivity);

    void inject(NewsDetailActivity newsDetailActivity);

}
