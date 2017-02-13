package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.news.NewsChannelTableBean;
import com.wsy.geeknewstest.model.bean.news.NewsManagerBean;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.presenter.contract.NewsMainContract;
import com.wsy.geeknewstest.util.RxUtil;
import com.wsy.geeknewstest.util.SharedPreferenceUtil;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hasee on 2017/1/17.
 */

public class NewsMainPresenter extends RxPresenter<NewsMainContract.View> implements NewsMainContract.Presenter {

    private RealmHelper mRealmHelper;
    private RealmList<NewsChannelTableBean> mList;

    @Inject
    public NewsMainPresenter(RealmHelper mRealmHelper) {
        this.mRealmHelper = mRealmHelper;
        registerEvent();
    }

    private void registerEvent() {
        Subscription rxSubscription = RxBus.getDefault().toObservable(NewsManagerBean.class)
                .compose(RxUtil.<NewsManagerBean>rxSchedulerHelper())
                .subscribe(new Action1<NewsManagerBean>() {
                    @Override
                    public void call(NewsManagerBean newsChannelTableBean) {
                        mRealmHelper.updateNewsManagerList(newsChannelTableBean);
                        mView.updateTab(newsChannelTableBean.getManagerList());
                    }
                });
        addSubscrebe(rxSubscription);
    }


    @Override
    public void initManagerList() {
        if (SharedPreferenceUtil.getNewsManagerPoint()) {
            //第一次使用，生成默认ManagerList
            initList();
            mRealmHelper.updateNewsManagerList(new NewsManagerBean(mList));
            mView.updateTab(mList);
            SharedPreferenceUtil.setNewsManagerPoint(false);
        } else {
            if (mRealmHelper.getNewsManagerBean() == null) {
                initList();
                mRealmHelper.updateNewsManagerList(new NewsManagerBean(mList));
            } else {
                mList = mRealmHelper.getNewsManagerBean().getManagerList();
            }
            mView.updateTab(mList);
        }
    }

    @Override
    public void setManagerList() {
        mView.jumpToManager(mRealmHelper.getNewsManagerBean());
    }

    private void initList() {
        mList = new RealmList<>();
        List<String> channelName = Arrays.asList(App.getInstance().getResources().getStringArray(R.array.news_channel_name));
        List<String> channelId = Arrays.asList(App.getInstance().getResources().getStringArray(R.array.news_channel_id));
        for (int i = 0; i < channelName.size(); i++) {
            mList.add(new NewsChannelTableBean(channelName.get(i), channelId.get(i), Constants.getType(channelId.get(i)), i <= 5, i, i == 0));
        }
    }
}
