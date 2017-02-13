package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.presenter.contract.LikeContract;

import javax.inject.Inject;

/**
 * Created by hasee on 2016/12/8.
 */

public class LikePresenter extends RxPresenter<LikeContract.View> implements LikeContract.Presenter {

    private RealmHelper mRealmHelper;

    @Inject
    public LikePresenter(RealmHelper mRealmHelper) {
        this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void getLikeData() {
        mView.showContent(mRealmHelper.getLikeList());
    }

    @Override
    public void deleteLikeData(String id) {
        mRealmHelper.deleteLikeBean(id);
    }

    @Override
    public void changeLikeTime(String id, long time, boolean isPlus) {
        mRealmHelper.changeLikeTime(id, time, isPlus);
    }
}
