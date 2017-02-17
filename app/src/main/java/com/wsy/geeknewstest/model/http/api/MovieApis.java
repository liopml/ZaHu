package com.wsy.geeknewstest.model.http.api;

import com.wsy.geeknewstest.model.bean.movie.MovieBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by hasee on 2017/2/14.
 */

public interface MovieApis {

    String HOST = "https://api.douban.com";

    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("/v2/movie/in_theaters")
    Observable<MovieBean> getMovieData();


}
