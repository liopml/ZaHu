package com.wsy.geeknewstest.model.http.api;

import com.wsy.geeknewstest.model.bean.news.NewsDetailBean;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;
import com.wsy.geeknewstest.model.http.NewsHttpResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by hasee on 2016/12/22.
 */

public interface NewsApis {

    String HOST = "http://c.m.163.com/";

    /**
     * example：http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     * <p>
     * Type ：headline为头条,house为房产，list为其他
     */
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummaryBean>>> getNewsList(@Path("type") String type,
                                                               @Path("id") String id,
                                                               @Path("startPage") int startPage
    );

    /**
     * example：http://c.m.163.com/nc/article/BG6CGA9M00264N2N/full.html
     */
    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetailBean>> getNewDetail(@Path("postId") String postId);

}
