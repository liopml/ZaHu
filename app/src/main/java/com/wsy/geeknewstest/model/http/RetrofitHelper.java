package com.wsy.geeknewstest.model.http;

import com.wsy.geeknewstest.BuildConfig;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.model.bean.gank.GankItemBean;
import com.wsy.geeknewstest.model.bean.gank.GankSearchItemBean;
import com.wsy.geeknewstest.model.bean.gold.GoldListBean;
import com.wsy.geeknewstest.model.bean.movie.MovieBean;
import com.wsy.geeknewstest.model.bean.news.NewsDetailBean;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;
import com.wsy.geeknewstest.model.bean.video.VideoDataBean;
import com.wsy.geeknewstest.model.bean.weixin.WXItemBean;
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
import com.wsy.geeknewstest.model.http.api.GankApis;
import com.wsy.geeknewstest.model.http.api.GoldApis;
import com.wsy.geeknewstest.model.http.api.MovieApis;
import com.wsy.geeknewstest.model.http.api.NewsApis;
import com.wsy.geeknewstest.model.http.api.NewsPhotoApis;
import com.wsy.geeknewstest.model.http.api.VideoApis;
import com.wsy.geeknewstest.model.http.api.WeChatApis;
import com.wsy.geeknewstest.model.http.api.ZhihuApis;
import com.wsy.geeknewstest.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by hasee on 2016/11/9.
 */

public class RetrofitHelper {

    private static OkHttpClient okHttpClient = null;
    private static ZhihuApis zhihuApiService = null;
    private static GankApis gankApiService = null;
    private static WeChatApis wechatApiService = null;
    private static GoldApis goldApiService = null;
    private static VideoApis videoApiService = null;
    private static NewsApis newsApiService = null;
    private static NewsPhotoApis newsPhotoService = null;
    private static MovieApis movieApiService = null;

    public RetrofitHelper() {
        init();
    }

    private void init() {
        initOkHttp();
        zhihuApiService = getZhihuApiService();
        gankApiService = getGankApiService();
        wechatApiService = getWechatApiService();
        goldApiService = getGoldApiService();
        videoApiService = getVideoApiService();
        newsApiService = getNewsApiService();
        newsPhotoService = getNewsPhotoService();
        movieApiService = getMovieApiService();
    }

    private static void initOkHttp() {
        // 参考 http://www.jianshu.com/p/93153b34310e
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //如果有些代码不想在发布后执行，就可以使用该功能 BuildConfig.DEBUG
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        //设置缓存路径和大小
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        //应用拦截器：设置缓存策略
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                //无网的时候强制使用缓存
                if (!SystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)  //只从缓存获取数据
                            .build();
                }

                //很重要，它是将拦截连串起来的关键。
                Response response = chain.proceed(request);

                // 有网络时, 不缓存, 最大保存时长为0
                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);  //自定义的应用拦截器
        builder.addInterceptor(cacheInterceptor);   //自定义的网络拦截器
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //以上设置结束，才能build(),不然设置白搭
        okHttpClient = builder.build();
    }

    //创建retrofit对象，返回的是实例化的Api对象
    private static ZhihuApis getZhihuApiService() {
        Retrofit zhihuRetrofit = new Retrofit.Builder()
                .baseUrl(ZhihuApis.HOST)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return zhihuRetrofit.create(ZhihuApis.class);
    }

    private static GankApis getGankApiService() {
        Retrofit gankRetrofit = new Retrofit.Builder()
                .baseUrl(GankApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return gankRetrofit.create(GankApis.class);
    }

    private static WeChatApis getWechatApiService() {
        Retrofit gankRetrofit = new Retrofit.Builder()
                .baseUrl(WeChatApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return gankRetrofit.create(WeChatApis.class);
    }

    private static GoldApis getGoldApiService() {
        Retrofit goldRetrofit = new Retrofit.Builder()
                .baseUrl(GoldApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return goldRetrofit.create(GoldApis.class);
    }

    private static VideoApis getVideoApiService() {
        Retrofit videoRetrofit = new Retrofit.Builder()
                .baseUrl(VideoApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return videoRetrofit.create(VideoApis.class);
    }

    private static NewsApis getNewsApiService() {
        Retrofit newsRetrofit = new Retrofit.Builder()
                .baseUrl(NewsApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return newsRetrofit.create(NewsApis.class);
    }

    private static NewsPhotoApis getNewsPhotoService() {
        Retrofit newsPhotoRetrofit = new Retrofit.Builder()
                .baseUrl(NewsPhotoApis.HTML_PHOTO)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return newsPhotoRetrofit.create(NewsPhotoApis.class);
    }

    private static MovieApis getMovieApiService() {
        Retrofit movieRetrofit = new Retrofit.Builder()
                .baseUrl(MovieApis.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return movieRetrofit.create(MovieApis.class);
    }

    //获取最新日报的信息
    public Observable<DailyListBean> fetchDailyListInfo() {
        return zhihuApiService.getDailyList();
    }

    //获取往期日报的信息
    public Observable<DailyBeforeListBean> fetchDailyBeforeListInfo(String date) {
        return zhihuApiService.getDailyBeforeList(date);
    }

    //获取主题日报的信息
    public Observable<ThemeListBean> fetchDailyThemeListInfo() {
        return zhihuApiService.getThemeList();
    }

    //获取主题日报详情的信息
    public Observable<ThemeChildListBean> fetchThemeChildListInfo(int id) {
        return zhihuApiService.getThemeChildList(id);
    }

    //获取专栏日报的信息
    public Observable<SectionListBean> fetchSectionListInfo() {
        return zhihuApiService.getSectionList();
    }

    //获取专栏日报详情的信息
    public Observable<SectionChildListBean> fetchSectionChildListInfo(int id) {
        return zhihuApiService.getSectionChildList(id);
    }

    //获取热门日报的信息
    public Observable<HotListBean> fetchHotListInfo() {
        return zhihuApiService.getHotList();
    }

    //获取热门日报详情的信息
    public Observable<ZhihuDetailBean> fetchDetailInfo(int id) {
        return zhihuApiService.getDetailInfo(id);
    }

    //获取日报的额外信息的信息
    public Observable<DetailExtraBean> fetchDetailExtraInfo(int id) {
        return zhihuApiService.getDetailExtraInfo(id);
    }

    //获取启动界面图片
    public Observable<WelcomeBean> fetchWelcomeInfo(String res) {
        return zhihuApiService.getWelcomeInfo(res);
    }

    //获取日报的长评论的信息
    public Observable<CommentBean> fetchLongCommentInfo(int id) {
        return zhihuApiService.getLongCommentInfo(id);
    }

    //获取日报的短评论的信息
    public Observable<CommentBean> fetchShortCommentInfo(int id) {
        return zhihuApiService.getShortCommentInfo(id);
    }

    public Observable<GankHttpResponse<List<GankItemBean>>> fetchTechList(String tech, int num, int page) {
        return gankApiService.getTechList(tech, num, page);
    }

    public Observable<GankHttpResponse<List<GankItemBean>>> fetchGirlList(int num, int page) {
        return gankApiService.getGirlList(num, page);
    }

    public Observable<GankHttpResponse<List<GankItemBean>>> fetchRandomGirl(int num) {
        return gankApiService.getRandomGirl(num);
    }

    public Observable<GankHttpResponse<List<GankSearchItemBean>>> fetchGankSearchList(String query, String type, int num, int page) {
        return gankApiService.getSearchList(query, type, num, page);
    }

    public Observable<WXHttpResponse<List<WXItemBean>>> fetchWechatListInfo(int num, int page) {
        return wechatApiService.getWXHot(Constants.KEY_API, num, page);
    }

    public Observable<WXHttpResponse<List<WXItemBean>>> fetchWechatSearchListInfo(int num, int page, String word) {
        return wechatApiService.getWXHotSearch(Constants.KEY_API, num, page, word);
    }

    public Observable<GoldHttpResponse<List<GoldListBean>>> fetchGoldList(String type, int num, int page) {
        return goldApiService.getGoldList("{\"category\":\"" + type + "\"}", "-createdAt", "user,user.installation", num, page * num);
    }

    public Observable<GoldHttpResponse<List<GoldListBean>>> fetchGoldHotList(String type, String dataTime, int limit) {
        return goldApiService.getGoldHot("{\"category\":\"" + type + "\",\"createdAt\":{\"$gt\":{\"__type\":\"Date\",\"iso\":\"" + dataTime + "T00:00:00.000Z\"}},\"objectId\":{\"$nin\":[\"58362f160ce463005890753e\",\"583659fcc59e0d005775cc8c\",\"5836b7358ac2470065d3df62\"]}}",
                "-hotIndex", "user,user.installation", limit);
    }

    public Observable<VideoDataBean> fetchVideoList() {
        return videoApiService.getVideoList();
    }

    //newsType ：headline为头条,house为房产，list为其他
    public Observable<Map<String, List<NewsSummaryBean>>> fetchNewsList(String newsType, String newsId, int startPage) {
        return newsApiService.getNewsList(newsType, newsId, startPage);
    }

    public Observable<Map<String, NewsDetailBean>> fetchNewsDetail(String postId) {
        return newsApiService.getNewDetail(postId);
    }

    public Observable<ResponseBody> fetchNewsBodyHtmlPhoto(String photoPath) {
        return newsPhotoService.getNewsBodyHtmlPhoto(photoPath);
    }

    public Observable<MovieBean> fetchMovieList() {
        return movieApiService.getMovieData();
    }

}
