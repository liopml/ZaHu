package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.news.NewsDetailBean;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.NewsDetailContract;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by hasee on 2017/2/6.
 */

public class NewsDetailPresenter extends RxPresenter<NewsDetailContract.View> implements NewsDetailContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public NewsDetailPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getNewsDetailData(final String id) {
        Subscription rxSubscription = mRetrofitHelper.fetchNewsDetail(id)
                .map(new Func1<Map<String, NewsDetailBean>, NewsDetailBean>() {
                    @Override
                    public NewsDetailBean call(Map<String, NewsDetailBean> stringNewsDetailBeanMap) {
                        NewsDetailBean newsDetailBean = stringNewsDetailBeanMap.get(id);
                        changeNewsDetail(newsDetailBean);
                        return newsDetailBean;
                    }
                })
                .compose(RxUtil.<NewsDetailBean>rxSchedulerHelper())
                .subscribe(new Action1<NewsDetailBean>() {
                    @Override
                    public void call(NewsDetailBean newsDetailBean) {
                        mView.showContent(newsDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载失败");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    private void changeNewsDetail(NewsDetailBean newsDetailBean) {
        List<NewsDetailBean.ImgBean> imgSrcs = newsDetailBean.getImg();
        if (isChange(imgSrcs)) {
            String newsBody = newsDetailBean.getBody();
            newsBody = changeNewsBody(imgSrcs, newsBody);
            newsDetailBean.setBody(newsBody);
        }
    }

    private boolean isChange(List<NewsDetailBean.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2;
    }

    private String changeNewsBody(List<NewsDetailBean.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);
        }
        return newsBody;
    }
}
