package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.movie.MovieBean;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.MovieContract;
import com.wsy.geeknewstest.util.RxUtil;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hasee on 2017/2/14.
 */

public class MoviePresenter extends RxPresenter<MovieContract.View> implements MovieContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public MoviePresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getMovieData() {
        Subscription rxSubscription = mRetrofitHelper.fetchMovieList()
                .compose(RxUtil.<MovieBean>rxSchedulerHelper())
                .subscribe(new Action1<MovieBean>() {
                    @Override
                    public void call(MovieBean movieBean) {
                        mView.showContent(movieBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载失败");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
