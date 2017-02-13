package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.news.NewsDetailBean;

/**
 * Created by hasee on 2017/2/6.
 */

public interface NewsDetailContract {

    interface View extends BaseView {

        void showContent(NewsDetailBean newsDetailBean);

    }

    interface Presenter extends BasePresenter<View> {

        void getNewsDetailData(String id);

    }
}
