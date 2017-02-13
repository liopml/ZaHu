package com.wsy.geeknewstest.model.http.api;

import com.wsy.geeknewstest.model.bean.weixin.WXItemBean;
import com.wsy.geeknewstest.model.http.WXHttpResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hasee on 2016/11/10.
 * 微信精选 http://www.tianapi.com/#wxnew
 */

public interface WeChatApis {

    String HOST = "http://api.tianapi.com/";

    /**
     * 微信精选列表
     */
    @GET("wxnew")
    Observable<WXHttpResponse<List<WXItemBean>>> getWXHot(@Query("key") String key, @Query("num") int num, @Query("page") int page);

    /**
     * 微信精选列表查询
     */
    @GET("wxnew")
    Observable<WXHttpResponse<List<WXItemBean>>> getWXHotSearch(@Query("key") String key, @Query("num") int num, @Query("page") int page, @Query("word") String word);
}
