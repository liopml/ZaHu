package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.VersionBean;

/**
 * Created by hasee on 2016/11/14.
 */

public interface MainContract {

    interface View extends BaseView {
        void showUpdateDialog(VersionBean bean);
    }

    interface Presenter extends BasePresenter<View> {
        void checkVersion(String currentVersion);
    }
}
