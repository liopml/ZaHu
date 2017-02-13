package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.zhihu.SectionChildListBean;

/**
 * Created by hasee on 2016/12/1.
 */

public interface SectionChildContract {

    interface View extends BaseView {

        void showContent(SectionChildListBean sectionChildListBean);

    }

    interface Presenter extends BasePresenter<View> {

        void getThemeChildData(int id);

        void insertReadToDB(int id);

    }
}
