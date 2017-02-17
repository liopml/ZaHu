package com.wsy.geeknewstest.presenter.contract;

import com.wsy.geeknewstest.base.BasePresenter;
import com.wsy.geeknewstest.base.BaseView;
import com.wsy.geeknewstest.model.bean.movie.MovieBean;

/**
 * Created by hasee on 2017/2/14.
 */

public interface MovieContract {

    interface View extends BaseView {

        void showContent(MovieBean movieData);

    }

    interface Presenter extends BasePresenter<View> {

        void getMovieData();

    }
}
