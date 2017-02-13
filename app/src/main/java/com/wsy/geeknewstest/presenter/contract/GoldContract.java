package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.gold.GoldListBean;

import java.util.List;

/**
 * Created by hasee on 2016/12/11.
 */

public interface GoldContract {

    interface View extends BaseView {

        void showContent(List<GoldListBean> goldListBean);

        void showMoreContent(List<GoldListBean> goldMoreListBean, int start, int end);
    }

    interface Presenter extends BasePresenter<View> {

        void getGoldData(String type);

        void getMoreGoldData();
    }


}
