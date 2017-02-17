package com.wsy.geeknewstest.di.component;

import android.app.Activity;

import com.wsy.geeknewstest.di.FragmentScope;
import com.wsy.geeknewstest.di.module.FragmentModule;
import com.wsy.geeknewstest.ui.gank.fargment.GirlFragment;
import com.wsy.geeknewstest.ui.gank.fargment.TechFragment;
import com.wsy.geeknewstest.ui.gold.fragment.GoldMainFragment;
import com.wsy.geeknewstest.ui.gold.fragment.GoldPagerFragment;
import com.wsy.geeknewstest.ui.main.fragment.LikeFragment;
import com.wsy.geeknewstest.ui.main.fragment.SettingFragment;
import com.wsy.geeknewstest.ui.movie.fragment.MovieMainFragment;
import com.wsy.geeknewstest.ui.news.fragment.NewsMainFragment;
import com.wsy.geeknewstest.ui.news.fragment.NewsPagerFragment;
import com.wsy.geeknewstest.ui.wechat.fragment.WechatMainFragment;
import com.wsy.geeknewstest.ui.zhihu.fragment.CommentFragment;
import com.wsy.geeknewstest.ui.zhihu.fragment.DailyFragment;
import com.wsy.geeknewstest.ui.zhihu.fragment.HotFragment;
import com.wsy.geeknewstest.ui.zhihu.fragment.SectionFragment;
import com.wsy.geeknewstest.ui.zhihu.fragment.ThemeFragment;

import dagger.Component;

/**
 * Created by hasee on 2016/11/20.
 * 用@Component表示这个接口是一个连接器，能用@Component注解的只能是interface或者抽象类
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    /**
     * 需要用到这个连接器的对象，就是这个对象里面有需要注入的属性（被标记为@Inject的属性）
     * 这里inject表示注入的意思，这个方法名可以随意更改，但建议就用inject即可。
     */
    void inject(DailyFragment dailyFragment);

    void inject(ThemeFragment themeFragment);

    void inject(SectionFragment sectionFragment);

    void inject(HotFragment hotFragment);

    void inject(CommentFragment longCommentFragment);

    void inject(WechatMainFragment wechatMainFragment);

    void inject(TechFragment techFragment);

    void inject(GirlFragment girlFragment);

    void inject(LikeFragment likeFragment);

    void inject(SettingFragment settingFragment);

    void inject(GoldMainFragment goldMainFragment);

    void inject(GoldPagerFragment goldPagerFragment);

    void inject(NewsMainFragment newsMainFragment);

    void inject(NewsPagerFragment newsPagerFragment);

    void inject(MovieMainFragment movieMainFragment);

}
