package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;
import com.wsy.geeknewstest.model.http.NewsHttpResponse;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.NewsContract;
import com.wsy.geeknewstest.util.DateUtil;
import com.wsy.geeknewstest.util.LogUtil;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by hasee on 2017/1/19.
 */

public class NewsPresenter extends RxPresenter<NewsContract.View> implements NewsContract.Presenter {

    private int NUM_EACH_PAGE;

    private RetrofitHelper mRetrofitHelper;
    private String mType;
    private String mId;

    @Inject
    public NewsPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getGoldData(String type, final String id) {
        mType = type;
        mId = id;
        NUM_EACH_PAGE = 0;
        Subscription rxSubscription = mRetrofitHelper.fetchNewsList(mType, mId, NUM_EACH_PAGE)
                .flatMap(new Func1<Map<String, List<NewsSummaryBean>>, Observable<NewsSummaryBean>>() {
                    @Override
                    public Observable<NewsSummaryBean> call(Map<String, List<NewsSummaryBean>> stringListMap) {
                        if (id.endsWith(Constants.HOUSE_ID)) {
                            // 房产实际上针对地区的它的id与返回key不同
                            return Observable.from(stringListMap.get("北京"));
                        }
                        return Observable.from(stringListMap.get(id));
                    }
                })
                .map(new Func1<NewsSummaryBean, NewsSummaryBean>() {
                    @Override
                    public NewsSummaryBean call(NewsSummaryBean newsSummaryBean) {
                        String time = DateUtil.formatDate(newsSummaryBean.getPtime());
                        newsSummaryBean.setPtime(time);
                        return newsSummaryBean;
                    }
                })
                .distinct()
                .toSortedList(new Func2<NewsSummaryBean, NewsSummaryBean, Integer>() {
                    @Override
                    public Integer call(NewsSummaryBean newsSummaryBean, NewsSummaryBean newsSummaryBean2) {
                        return newsSummaryBean2.getPtime().compareTo(newsSummaryBean.getPtime());
                    }
                })
                .compose(RxUtil.<List<NewsSummaryBean>>rxSchedulerHelper())
                .subscribe(new Action1<List<NewsSummaryBean>>() {
                    @Override
                    public void call(List<NewsSummaryBean> newsSummaryBeanList) {
                        mView.showContent(newsSummaryBeanList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.d(throwable.toString());
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getMoreGoldData() {
        NUM_EACH_PAGE += 20;
        Subscription rxSubscription = mRetrofitHelper.fetchNewsList(mType, mId, NUM_EACH_PAGE)
                .flatMap(new Func1<Map<String, List<NewsSummaryBean>>, Observable<NewsSummaryBean>>() {
                    @Override
                    public Observable<NewsSummaryBean> call(Map<String, List<NewsSummaryBean>> stringListMap) {
                        if (mId.endsWith(Constants.HOUSE_ID)) {
                            // 房产实际上针对地区的它的id与返回key不同
                            return Observable.from(stringListMap.get("北京"));
                        }
                        return Observable.from(stringListMap.get(mId));
                    }
                })
                .map(new Func1<NewsSummaryBean, NewsSummaryBean>() {
                    @Override
                    public NewsSummaryBean call(NewsSummaryBean newsSummaryBean) {
                        String time = DateUtil.formatDate(newsSummaryBean.getPtime());
                        newsSummaryBean.setPtime(time);
                        return newsSummaryBean;
                    }
                })
                .distinct()
                .toSortedList(new Func2<NewsSummaryBean, NewsSummaryBean, Integer>() {
                    @Override
                    public Integer call(NewsSummaryBean newsSummaryBean, NewsSummaryBean newsSummaryBean2) {
                        return newsSummaryBean2.getPtime().compareTo(newsSummaryBean.getPtime());
                    }
                })
                .compose(RxUtil.<List<NewsSummaryBean>>rxSchedulerHelper())
                .subscribe(new Action1<List<NewsSummaryBean>>() {
                    @Override
                    public void call(List<NewsSummaryBean> newsSummaryBeanList) {
                        mView.showMoreContent(newsSummaryBeanList);
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
