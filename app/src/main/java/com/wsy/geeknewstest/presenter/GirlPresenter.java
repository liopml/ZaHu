package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.gank.GankItemBean;
import com.wsy.geeknewstest.model.http.GankHttpResponse;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.GirlContract;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hasee on 2016/12/7.
 */

public class GirlPresenter extends RxPresenter<GirlContract.View> implements GirlContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    public static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;

    @Inject
    public GirlPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getGirlData() {
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchGirlList(NUM_OF_PAGE, currentPage)
                .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleResult())
                .subscribe(new Action1<List<GankItemBean>>() {
                    @Override
                    public void call(List<GankItemBean> list) {
                        mView.showContent(list);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getMoreGirlData() {
        Subscription rxSubscription = mRetrofitHelper.fetchGirlList(NUM_OF_PAGE, ++currentPage)
                .compose(RxUtil.<GankHttpResponse<List<GankItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GankItemBean>>handleResult())
                .subscribe(new Action1<List<GankItemBean>>() {
                    @Override
                    public void call(List<GankItemBean> list) {
                        mView.showMoreContent(list);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载更多数据失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
