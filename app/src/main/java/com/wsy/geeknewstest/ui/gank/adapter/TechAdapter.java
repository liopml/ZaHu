package com.wsy.geeknewstest.ui.gank.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.model.bean.gank.GankItemBean;
import com.wsy.geeknewstest.presenter.TechPresenter;
import com.wsy.geeknewstest.util.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2016/12/4.
 */

public class TechAdapter extends RecyclerView.Adapter<TechAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<GankItemBean> mList;
    private OnItemClickListener onItemClickListener;

    private String tech;

    public TechAdapter(Context mContext, List<GankItemBean> mList, String tech) {
        inflater = LayoutInflater.from(mContext);
        this.mList = mList;
        this.tech = tech;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_tech, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (tech.equals(GankMainAdapter.tabTitle[0])) {
            holder.ivIcon.setImageResource(R.mipmap.ic_android);
        } else if (tech.equals(GankMainAdapter.tabTitle[1])) {
            holder.ivIcon.setImageResource(R.mipmap.ic_ios);
        } else if (tech.equals(GankMainAdapter.tabTitle[2])) {
            holder.ivIcon.setImageResource(R.mipmap.ic_web);
        }
        holder.tvContent.setText(mList.get(position).getDesc());
        holder.tvAuthor.setText(mList.get(position).getWho());
        String date = mList.get(position).getPublishedAt();
        int idx = date.indexOf(".");
        date = date.substring(0, idx).replace("T", " ");
        holder.tvTime.setText(DateUtil.formatDateTime(date, true));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    CardView cv = (CardView) v.findViewById(R.id.cv_tech_content);
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), cv);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_tech_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_tech_title)
        TextView tvContent;
        @BindView(R.id.tv_tech_author)
        TextView tvAuthor;
        @BindView(R.id.tv_tech_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
