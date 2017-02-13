package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.NightModeEvent;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.MainContract;
import com.wsy.geeknewstest.util.RxUtil;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by hasee on 2016/11/14.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public MainPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    public void registerEvent() {
        Subscription rxSubscription = RxBus.getDefault().toObservable(NightModeEvent.class)
                .compose(RxUtil.<NightModeEvent>rxSchedulerHelper())
                .map(new Func1<NightModeEvent, Boolean>() {
                    @Override
                    public Boolean call(NightModeEvent nightModeEvent) {
                        return nightModeEvent.getNightMode();
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError("切换模式失败ヽ(≧Д≦)ノ");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.useNightMode(aBoolean);
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void checkVersion(final String currentVersion) {


    }
}
