package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.zhihu.DailyBeforeListBean;
import com.wsy.geeknewstest.model.bean.zhihu.DailyListBean;

/**
 * Created by hasee on 2016/11/20.
 */

public interface DailyContract {

    interface View extends BaseView {

        void showContent(DailyListBean info);

        void showMoreContent(String date, DailyBeforeListBean info);

        void showProgress();

        void doInterval(int currentCount);
    }

    interface Presenter extends BasePresenter<View> {

        void getDailyData();

        void getBeforeData(String date);

        void startInterval();

        void stopInterval();

        void insertReadToDB(int id);
    }
}
