package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.RealmLikeBean;
import com.wsy.geeknewstest.model.bean.zhihu.DetailExtraBean;
import com.wsy.geeknewstest.model.bean.zhihu.ZhihuDetailBean;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.ZhihuDetailContract;
import com.wsy.geeknewstest.util.RxUtil;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hasee on 2016/11/25.
 */

public class ZhihuDetailPresenter extends RxPresenter<ZhihuDetailContract.View> implements ZhihuDetailContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;
    private ZhihuDetailBean mData;

    @Inject
    public ZhihuDetailPresenter(RetrofitHelper mRetrofitHelper, RealmHelper mRealmHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        this.mRealmHelper = mRealmHelper;
    }


    @Override
    public void getDetailData(int id) {
        Subscription rxSubscription = mRetrofitHelper.fetchDetailInfo(id)
                .compose(RxUtil.<ZhihuDetailBean>rxSchedulerHelper())
                .subscribe(new Action1<ZhihuDetailBean>() {
                    @Override
                    public void call(ZhihuDetailBean zhihuDetailBean) {
                        mData = zhihuDetailBean;
                        mView.showContent(zhihuDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载数据失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getExtraData(int id) {
        Subscription rxSubscription = mRetrofitHelper.fetchDetailExtraInfo(id)
                .compose(RxUtil.<DetailExtraBean>rxSchedulerHelper())
                .subscribe(new Action1<DetailExtraBean>() {
                    @Override
                    public void call(DetailExtraBean detailExtraBean) {
                        mView.showExtraInfo(detailExtraBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载额外信息失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void insertLikeData() {
        if (mData != null) {
            RealmLikeBean bean = new RealmLikeBean();
            bean.setId(String.valueOf(mData.getId()));
            bean.setImage(mData.getImage());
            bean.setTitle(mData.getTitle());
            bean.setType(Constants.TYPE_ZHIHU);
            bean.setTime(System.currentTimeMillis());
            mRealmHelper.insertLikeBean(bean);
        } else {
            mView.showError("操作失败");
        }
    }

    @Override
    public void deleteLikeData() {
        if (mData != null) {
            mRealmHelper.deleteLikeBean(String.valueOf(mData.getId()));
        } else {
            mView.showError("操作失败");
        }
    }

    @Override
    public void queryLikeData(int id) {
        mView.setLikeButtonState(mRealmHelper.queryLikeId(String.valueOf(id)));
    }
}
