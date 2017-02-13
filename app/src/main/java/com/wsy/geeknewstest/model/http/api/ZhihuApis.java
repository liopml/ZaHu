package com.wsy.geeknewstest.model.http.api;

import com.wsy.geeknewstest.model.bean.zhihu.CommentBean;
import com.wsy.geeknewstest.model.bean.zhihu.DailyBeforeListBean;
import com.wsy.geeknewstest.model.bean.zhihu.DailyListBean;
import com.wsy.geeknewstest.model.bean.zhihu.DetailExtraBean;
import com.wsy.geeknewstest.model.bean.zhihu.HotListBean;
import com.wsy.geeknewstest.model.bean.zhihu.SectionChildListBean;
import com.wsy.geeknewstest.model.bean.zhihu.SectionListBean;
import com.wsy.geeknewstest.model.bean.zhihu.ThemeChildListBean;
import com.wsy.geeknewstest.model.bean.zhihu.ThemeListBean;
import com.wsy.geeknewstest.model.bean.zhihu.WelcomeBean;
import com.wsy.geeknewstest.model.bean.zhihu.ZhihuDetailBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hasee on 2016/11/9.
 * 知乎APIs  https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90
 */

public interface ZhihuApis {

    String HOST = "http://news-at.zhihu.com/api/4/";

    /**
     * 启动界面图片
     */
    @GET("start-image/{res}")
    Observable<WelcomeBean> getWelcomeInfo(@Path("res") String res);

    /**
     * 最新日报
     */
    @GET("news/latest")
    Observable<DailyListBean> getDailyList();

    /**
     * 往期日报
     */
    @GET("news/before/{date}")
    Observable<DailyBeforeListBean> getDailyBeforeList(@Path("date") String date);

    /**
     * 主题日报
     */
    @GET("themes")
    Observable<ThemeListBean> getThemeList();

    /**
     * 主题日报详情
     */
    @GET("theme/{id}")
    Observable<ThemeChildListBean> getThemeChildList(@Path("id") int id);

    /**
     * 专栏日报
     */
    @GET("sections")
    Observable<SectionListBean> getSectionList();

    /**
     * 专栏日报详情
     */
    @GET("section/{id}")
    Observable<SectionChildListBean> getSectionChildList(@Path("id") int id);

    /**
     * 热门日报
     */
    @GET("news/hot")
    Observable<HotListBean> getHotList();

    /**
     * 热门日报详情
     */
    @GET("news/{id}")
    Observable<ZhihuDetailBean> getDetailInfo(@Path("id") int id);

    /**
     * 日报的额外信息
     */
    @GET("story-extra/{id}")
    Observable<DetailExtraBean> getDetailExtraInfo(@Path("id") int id);

    /**
     * 日报的长评论
     */
    @GET("story/{id}/long-comments")
    Observable<CommentBean> getLongCommentInfo(@Path("id") int id);

    /**
     * 日报的短评论
     */
    @GET("story/{id}/short-comments")
    Observable<CommentBean> getShortCommentInfo(@Path("id") int id);
}
