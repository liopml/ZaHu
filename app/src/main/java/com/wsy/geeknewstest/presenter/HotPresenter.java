package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.zhihu.HotListBean;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.HotContract;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by hasee on 2016/11/25.
 */

public class HotPresenter extends RxPresenter<HotContract.View> implements HotContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private RealmHelper mRealmHelper;

    @Inject
    public HotPresenter(RetrofitHelper mRetrofitHelper, RealmHelper mRealmHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        this.mRealmHelper = mRealmHelper;
    }

    @Override
    public void getHotData() {
        Subscription rxSubscription = mRetrofitHelper.fetchHotListInfo()
                .compose(RxUtil.<HotListBean>rxSchedulerHelper())
                .map(new Func1<HotListBean, HotListBean>() {
                    @Override
                    public HotListBean call(HotListBean hotListBean) {
                        List<HotListBean.RecentBean> list = hotListBean.getRecent();
                        for (HotListBean.RecentBean item : list) {
                            item.setReadState(mRealmHelper.queryNewsId(item.getNews_id()));
                        }
                        return hotListBean;
                    }
                })
                .subscribe(new Action1<HotListBean>() {
                    @Override
                    public void call(HotListBean hotListBean) {
                        mView.showContent(hotListBean);
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
    public void insertReadToDB(int id) {
        mRealmHelper.insertNewsId(id);
    }
}
