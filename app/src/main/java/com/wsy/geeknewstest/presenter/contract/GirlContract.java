package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.gank.GankItemBean;

import java.util.List;

/**
 * Created by hasee on 2016/12/7.
 */

public interface GirlContract {

    interface View extends BaseView {

        void showContent(List<GankItemBean> list);

        void showMoreContent(List<GankItemBean> list);

    }

    interface Presenter extends BasePresenter<View>{

        void getGirlData();

        void getMoreGirlData();
    }
}
