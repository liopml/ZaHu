package com.wsy.geeknewstest.presenter;

import com.wsy.geeknewstest.base.RxPresenter;
import com.wsy.geeknewstest.model.bean.video.VideoDataBean;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.presenter.contract.VideoContract;
import com.wsy.geeknewstest.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by hasee on 2016/12/15.
 */

public class VideoPresenter extends RxPresenter<VideoContract.View> implements VideoContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public VideoPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void getVideoData() {
        Subscription rxSubscription = mRetrofitHelper.fetchVideoList()
                .compose(RxUtil.<VideoDataBean>rxSchedulerHelper())
                .map(new Func1<VideoDataBean, VideoDataBean>() {
                    @Override
                    public VideoDataBean call(VideoDataBean videoDataBean) {
                        List<VideoDataBean.Data.DataBean> list = videoDataBean.getData().getData();
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getType() != 1) {  //过滤掉不是视频的数据
                                    list.remove(i);
                                }
                            }
                        }
                        return videoDataBean;
                    }
                })
                .subscribe(new Action1<VideoDataBean>() {
                    @Override
                    public void call(VideoDataBean videoDataBean) {
                        mView.showContent(videoDataBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("加载视频失败");
                    }
                });
        addSubscrebe(rxSubscription);
    }

}
