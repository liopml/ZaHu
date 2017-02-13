package com.wsy.geeknewstest.model.http.api;

import com.wsy.geeknewstest.model.bean.video.VideoDataBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hasee on 2016/12/14.
 * 视频接口
 */

public interface VideoApis {

    String HOST = "http://is.snssdk.com/";

    @GET("neihan/stream/mix/v1/?mpic=1&webp=1&essence=1&content_type=-104&message_cursor=-1")
    Observable<VideoDataBean> getVideoList();
}
