package com.wsy.geeknewstest.base;

/**
 * Created by hasee on 2016/11/5.
 * Presenter基类
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();

}
