package com.wsy.geeknewstest.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hasee on 2016/11/9.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */

public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;
    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {  //取消订阅
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    protected void addSubscrebe(Subscription subscription) {  //订阅
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }


    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
