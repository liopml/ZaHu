package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.gank.GankItemBean;

import java.util.List;

/**
 * Created by hasee on 2016/12/4.
 */

public interface TechContract {

    interface View extends BaseView {

        void showContent(List<GankItemBean> mList);

        void showMoreContent(List<GankItemBean> mList);

        void showGirlImage(String url, String copyright);
    }

    interface Presenter extends BasePresenter<View> {

        void getGankData(String tech, int type);

        void getMoreGankData(String tech);

        void getGirlImage();

    }
}
