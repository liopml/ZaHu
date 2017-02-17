package com.wsy.geeknewstest.ui.movie.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.component.ImageLoader;
import com.wsy.geeknewstest.model.bean.movie.SubjectsBean;
import com.wsy.geeknewstest.ui.movie.activity.MovieDetailActivity;
import com.wsy.geeknewstest.util.StringFormatUtil;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2017/2/15.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 电影栏头部的图片
    public static final String ONE_URL_01 = "http://ojyz0c8un.bkt.clouddn.com/one_01.png";

    private static final int TYPE_MOVIE_HOLDER = 0;
    private static final int TYPE_MOVIE_CONTENT = 1;

    private List<SubjectsBean> mList;
    private LayoutInflater inflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private Activity mActivity;

    public MovieAdapter(Context mContext, List<SubjectsBean> mList, Activity mActivity) {
        inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = mList;
        this.mActivity = mActivity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MOVIE_HOLDER;
        } else {
            return TYPE_MOVIE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MOVIE_HOLDER:
                view = inflater.inflate(R.layout.item_movie_holder, parent, false);
                OneViewHolder oneViewHolder = new OneViewHolder(view);
                return oneViewHolder;
            case TYPE_MOVIE_CONTENT:
                view = inflater.inflate(R.layout.item_movie, parent, false);
                ContentViewHolder contentViewHolder = new ContentViewHolder(view);
                return contentViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OneViewHolder) {
            ((OneViewHolder) holder).llMovieTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ImageLoader.load(mContext, ONE_URL_01, ((OneViewHolder) holder).ivImg);
        } else if (holder instanceof ContentViewHolder) {
            final SubjectsBean SubjectsData = mList.get(position);

            String img = SubjectsData.getImages().getLarge();
            String title = SubjectsData.getTitle();
            String directors = StringFormatUtil.formatName(SubjectsData.getDirectors());
            String casts = StringFormatUtil.formatName(SubjectsData.getCasts());
            String genres = "类型：" + StringFormatUtil.formatGenres(SubjectsData.getGenres());
            String grade = "评分：" + SubjectsData.getRating().getAverage();

            ImageLoader.load(mContext, img, ((ContentViewHolder) holder).moviePhoto);
            ((ContentViewHolder) holder).movieTitle.setText(title);
            ((ContentViewHolder) holder).movieDirectors.setText(directors);
            ((ContentViewHolder) holder).movieCasts.setText(casts);
            ((ContentViewHolder) holder).movieGenres.setText(genres);
            ((ContentViewHolder) holder).movieGrade.setText(grade);

            ViewHelper.setScaleX(holder.itemView, 0.8f);
            ViewHelper.setScaleY(holder.itemView, 0.8f);
            ViewPropertyAnimator.animate(holder.itemView).scaleX(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
            ViewPropertyAnimator.animate(holder.itemView).scaleY(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra("data", SubjectsData);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, ((ContentViewHolder) holder).moviePhoto
                            , App.getInstance().getResources().getString(R.string.transition_movie_img));
                    ActivityCompat.startActivity(mContext, intent, options.toBundle());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class OneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_movie_top)
        View llMovieTop;
        @BindView(R.id.iv_img)
        ImageView ivImg;

        public OneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_movie_photo)
        ImageView moviePhoto;
        @BindView(R.id.tv_movie_title)
        TextView movieTitle;
        @BindView(R.id.tv_movie_directors)
        TextView movieDirectors;
        @BindView(R.id.tv_movie_casts)
        TextView movieCasts;
        @BindView(R.id.tv_movie_genres)
        TextView movieGenres;
        @BindView(R.id.tv_movie_grade)
        TextView movieGrade;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void updateData(List<SubjectsBean> list) {
        mList = list;
    }
}
