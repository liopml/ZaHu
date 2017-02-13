package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.zhihu.WelcomeBean;

/**
 * Created by hasee on 2016/11/9.
 */

public interface WelcomeContract {

    interface View extends BaseView {

        void showContent(WelcomeBean welcomeBean);

        void jumpToMain();

    }

    interface Presenter extends BasePresenter<View> {

        void getWelcomeData();

    }
}
