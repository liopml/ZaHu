package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.SearchEvent;
import com.wsy.geeknewstest.model.bean.weixin.WXItemBean;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.model.http.WXHttpResponse;
import com.wsy.geeknewstest.presenter.contract.WechatContract;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by hasee on 2016/12/1.
 */

public class WechatPresenter extends RxPresenter<WechatContract.View> implements WechatContract.Presenter {

    public static final String TECH_WECHAT = "微信";
    private static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;
    private String queryStr = null;

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public WechatPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    @Override
    public void getWechatData() {
        queryStr = null;
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchWechatListInfo(NUM_OF_PAGE, currentPage)
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribe(new Action1<List<WXItemBean>>() {
                    @Override
                    public void call(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
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
    public void getMoreWechatData() {
        Observable<WXHttpResponse<List<WXItemBean>>> observable;
        if (queryStr != null) {
            observable = mRetrofitHelper.fetchWechatSearchListInfo(NUM_OF_PAGE, ++currentPage, queryStr);
        } else {
            observable = mRetrofitHelper.fetchWechatListInfo(NUM_OF_PAGE, ++currentPage);
        }
        Subscription rxSubscription = observable
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribe(new Action1<List<WXItemBean>>() {
                    @Override
                    public void call(List<WXItemBean> wxItemBeen) {
                        mView.showMoreContent(wxItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("没有更多了ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    void registerEvent() {
        Subscription rxSubscription = RxBus.getDefault().toObservable(SearchEvent.class)
                .compose(RxUtil.<SearchEvent>rxSchedulerHelper())
                .filter(new Func1<SearchEvent, Boolean>() {
                    @Override
                    public Boolean call(SearchEvent searchEvent) {
                        return searchEvent.getType() == Constants.TYPE_WECHAT;
                    }
                })
                .map(new Func1<SearchEvent, String>() {
                    @Override
                    public String call(SearchEvent searchEvent) {
                        return searchEvent.getQuery();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        queryStr = s;
                        getSearchWechatData(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("搜索失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    private void getSearchWechatData(String query) {
        currentPage = 1;
        Subscription rxSubscription = mRetrofitHelper.fetchWechatSearchListInfo(NUM_OF_PAGE, currentPage, query)
                .compose(RxUtil.<WXHttpResponse<List<WXItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<WXItemBean>>handleWXResult())
                .subscribe(new Action1<List<WXItemBean>>() {
                    @Override
                    public void call(List<WXItemBean> wxItemBeen) {
                        mView.showContent(wxItemBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
        addSubscrebe(rxSubscription);
    }


}
