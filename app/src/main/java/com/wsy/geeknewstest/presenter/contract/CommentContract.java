package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.zhihu.CommentBean;

/**
 * Created by hasee on 2016/11/27.
 */

public interface CommentContract {

    interface View extends BaseView {

        void showContent(CommentBean commentBean);

    }

    interface Presenter extends BasePresenter<View> {

        void getCommentData(int id, int commentKind);

    }
}
