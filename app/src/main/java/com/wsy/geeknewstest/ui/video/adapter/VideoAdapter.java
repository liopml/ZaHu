package com.wsy.geeknewstest.ui.video.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.model.bean.video.VideoDataBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2016/12/15.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    public final static String TAG = "RecyclerView2List";

    private Context mContext;
    private LayoutInflater inflater;
    private List<VideoDataBean.Data.DataBean> mList;

    ImageView imageView;

    private ListVideoUtil listVideoUtil;

    public VideoAdapter(Context mContext, List<VideoDataBean.Data.DataBean> mList, ListVideoUtil listVideoUtil) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.listVideoUtil = listVideoUtil;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mList.get(position).getGroup() != null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.title.setText(mList.get(position).getGroup().getText());
            holder.type.setText(mList.get(position).getGroup().getCategory_name());
            holder.from.setText(mList.get(position).getGroup().getPlay_count() + "次播放");
            if (mList.get(position).getGroup().getLarge_cover().getUrl_list().get(0).getUrl().contains("http")) {
                Glide.with(mContext).load(mList.get(position).getGroup().getLarge_cover().getUrl_list().get(0).getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
            } else {
                Glide.with(mContext).load("http://www.zbjuran.com/" + mList.get(position).getGroup().getLarge_cover().getUrl_list().get(0).getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
            }
            onBind(position, holder);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fl_video)
        FrameLayout videoLayout;
        @BindView(R.id.video_title)
        TextView title;
        @BindView(R.id.tv_from)
        TextView from;
        @BindView(R.id.tv_type)
        TextView type;
        @BindView(R.id.image_play)
        ImageView listItemBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void onBind(final int position, ViewHolder holder) {
        //动态添加视频播放
        listVideoUtil.addVideoPlayer(position, imageView, TAG, holder.videoLayout, holder.listItemBtn);
        holder.listItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                //设置列表播放中的位置和TAG,防止错位，回复播放位置
                listVideoUtil.setPlayPositionAndTag(position, TAG);
                final String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
                //开始播放
                listVideoUtil.startPlay(url);
            }
        });
    }

}
