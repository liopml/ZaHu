package com.wsy.geeknewstest.util;


import com.wsy.geeknewstest.model.http.ApiException;
import com.wsy.geeknewstest.model.http.GankHttpResponse;
import com.wsy.geeknewstest.model.http.GoldHttpResponse;
import com.wsy.geeknewstest.model.http.NewsHttpResponse;
import com.wsy.geeknewstest.model.http.WXHttpResponse;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by hasee on 2016/11/12.
 */

public class RxUtil {

    /**
     * 统一线程处理
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {   //compose简化线程
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理(微信)
     */
    public static <T> Observable.Transformer<WXHttpResponse<T>, T> handleWXResult() {  //compose判断结果
        return new Observable.Transformer<WXHttpResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<WXHttpResponse<T>> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<WXHttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(WXHttpResponse<T> tWXHttpResponse) {
                        if (tWXHttpResponse.getCode() == 200) {
                            return createData(tWXHttpResponse.getNewslist());
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一返回结果处理(Gank)
     */
    public static <T> Observable.Transformer<GankHttpResponse<T>, T> handleResult() {   //compose判断结果
        return new Observable.Transformer<GankHttpResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<GankHttpResponse<T>> gankHttpResponseObservable) {
                return gankHttpResponseObservable.flatMap(new Func1<GankHttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(GankHttpResponse<T> tGankHttpResponse) {
                        if (!tGankHttpResponse.getError()) {
                            return createData(tGankHttpResponse.getResults());
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一返回结果处理(gold)
     */
    public static <T> Observable.Transformer<GoldHttpResponse<T>, T> handleGoldResult() {  //compose判断结果
        return new Observable.Transformer<GoldHttpResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<GoldHttpResponse<T>> goldHttpResponseObservable) {
                return goldHttpResponseObservable.flatMap(new Func1<GoldHttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(GoldHttpResponse<T> tGoldHttpResponse) {
                        if (tGoldHttpResponse.getResults() != null) {
                            return createData(tGoldHttpResponse.getResults());
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一返回结果处理(news)
     */
    public static <T> Observable.Transformer<NewsHttpResponse<T>, T> handleNewsResult() {  //compose判断结果
        return new Observable.Transformer<NewsHttpResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<NewsHttpResponse<T>> newsHttpResponseObservable) {
                return newsHttpResponseObservable.flatMap(new Func1<NewsHttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(NewsHttpResponse<T> tNewsHttpResponse) {
                        if (tNewsHttpResponse.getResults() != null) {
                            return createData(tNewsHttpResponse.getResults());
                        } else {
                            return Observable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Observable
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
