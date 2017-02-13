package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.news.NewsChannelTableBean;
import com.wsy.geeknewstest.model.bean.news.NewsManagerBean;

import java.util.List;

/**
 * Created by hasee on 2016/12/22.
 */

public interface NewsMainContract {

    interface View extends BaseView {

        void updateTab(List<NewsChannelTableBean> mList);

        void jumpToManager(NewsManagerBean mList);
    }

    interface Presenter extends BasePresenter<View> {

        void initManagerList();

        void setManagerList();
    }
}
