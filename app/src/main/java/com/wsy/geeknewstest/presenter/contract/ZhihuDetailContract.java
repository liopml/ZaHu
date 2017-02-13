package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.zhihu.DetailExtraBean;
import com.wsy.geeknewstest.model.bean.zhihu.ZhihuDetailBean;

/**
 * Created by hasee on 2016/11/25.
 */

public interface ZhihuDetailContract {

    interface View extends BaseView {

        void showContent(ZhihuDetailBean zhihuDetailBean);

        void showExtraInfo(DetailExtraBean detailExtraBean);

        void setLikeButtonState(boolean isLiked);
    }

    interface Presenter extends BasePresenter<View> {

        void getDetailData(int id);

        void getExtraData(int id);

        void insertLikeData();

        void deleteLikeData();

        void queryLikeData(int id);
    }
}
