package com.wsy.geeknewstest.model.http.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by hasee on 2017/2/6.
 */

public interface NewsPhotoApis {

    String HTML_PHOTO = "http://kaku.com/";

    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    // baseUrl 需要符合标准，为空、""、或不合法将会报错
    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(@Url String photoPath);
}
