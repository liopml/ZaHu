package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;

import java.util.List;

/**
 * Created by hasee on 2017/1/19.
 */

public interface NewsContract {

    interface View extends BaseView {

        void showContent(List<NewsSummaryBean> newsSummaryBeanList);

        void showMoreContent(List<NewsSummaryBean> newsSummaryBeanMoreList);
    }

    interface Presenter extends BasePresenter<View> {

        void getGoldData(String type, String id);

        void getMoreGoldData();
    }

}
