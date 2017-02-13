package com.wsy.geeknewstest.ui.zhihu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.component.ImageLoader;
import com.wsy.geeknewstest.model.bean.zhihu.ThemeChildListBean;
import com.wsy.geeknewstest.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2016/11/28.
 */

public class ThemeChildAdapter extends RecyclerView.Adapter<ThemeChildAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<ThemeChildListBean.StoriesBean> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ThemeChildAdapter(Context mContext, List<ThemeChildListBean.StoriesBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mList.get(position).getImages() != null && mList.get(position).getImages().size() > 0) {
            ImageLoader.load(mContext, mList.get(position).getImages().get(0), holder.image);
        }
        holder.title.setText(mList.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    SquareImageView iv = (SquareImageView) v.findViewById(R.id.iv_daily_item_image);
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), iv);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_daily_item_title)
        TextView title;
        @BindView(R.id.iv_daily_item_image)
        SquareImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setReadState(int position, boolean readState) {
        mList.get(position).setReadState(readState);
    }
}
