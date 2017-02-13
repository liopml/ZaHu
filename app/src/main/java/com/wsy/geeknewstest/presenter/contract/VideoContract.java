package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.video.VideoDataBean;

import java.util.List;

/**
 * Created by hasee on 2016/12/14.
 */

public interface VideoContract {

    interface View extends BaseView {

        void showContent(VideoDataBean mList);

    }

    interface Presenter extends BasePresenter<View> {

        void getVideoData();

    }
}

