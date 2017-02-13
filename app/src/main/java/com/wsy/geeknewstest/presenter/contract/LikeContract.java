package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.RealmLikeBean;

import java.util.List;

/**
 * Created by hasee on 2016/12/8.
 */

public interface LikeContract {

    interface View extends BaseView {

        void showContent(List<RealmLikeBean> mList);

    }

    interface Presenter extends BasePresenter<View>{

        void getLikeData();

        void deleteLikeData(String id);

        void changeLikeTime(String id, long time, boolean isPlus);

    }
}
