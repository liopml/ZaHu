package com.wsy.geeknewstest.ui.video.activity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseActivity;
import com.wsy.geeknewstest.listener.SampleListener;
import com.wsy.geeknewstest.model.bean.video.VideoDataBean;
import com.wsy.geeknewstest.presenter.VideoPresenter;
import com.wsy.geeknewstest.presenter.contract.VideoContract;
import com.wsy.geeknewstest.ui.video.adapter.VideoAdapter;
import com.wsy.geeknewstest.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hasee on 2016/12/18.
 */

public class VideoActivity extends BaseActivity<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.list_item_recycler)
    RecyclerView listItemRecycler;
    @BindView(R.id.video_full_container)
    FrameLayout videoFullContainer;

    LinearLayoutManager linearLayoutManager;

    VideoAdapter mAdapter;

    List<VideoDataBean.Data.DataBean> mList;

    ListVideoUtil listVideoUtil;
    int lastVisibleItem;
    int firstVisibleItem;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void initEventAndData() {
        mList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(mContext);
        listVideoUtil = new ListVideoUtil(mContext);
        listItemRecycler.setLayoutManager(linearLayoutManager);
        mAdapter = new VideoAdapter(mContext, mList, listVideoUtil);
        listItemRecycler.setAdapter(mAdapter);
        //设置全屏显示的viewGroup
        listVideoUtil.setFullViewContainer(videoFullContainer);
        listVideoUtil.setHideStatusBar(true);
        listItemRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放,对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals(VideoAdapter.TAG)) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!listVideoUtil.isSmall()) {
                            int size = SystemUtil.dp2px(mContext, 150);
                            //actionbar为true才不会掉下面去
                            listVideoUtil.showSmallVideo(new Point(size, size), true, true);
                        } else {
                            if (listVideoUtil.isSmall()) {
                                //恢复小屏幕效果
                                listVideoUtil.smallVideoToNormal();
                            }
                        }
                    }
                }
            }
        });
        //小窗口关闭被点击的时候回调处理回复页面
        listVideoUtil.setVideoAllCallBack(new SampleListener() {
            @Override
            public void onQuitSmallWidget(String s, Object... objects) {
                super.onQuitSmallWidget(s, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals(VideoAdapter.TAG)) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        listVideoUtil.releaseVideoPlayer();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        mPresenter.getVideoData();
    }

    @Override
    public void onBackPressed() {
        if (listVideoUtil.backFromFull()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listVideoUtil.releaseVideoPlayer();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void showContent(VideoDataBean list) {
        mList.clear();
        mList.addAll(list.getData().getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {

    }
}
