package com.wsy.geeknewstest.ui.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.base.BaseRecyclerViewAdapter;
import com.wsy.geeknewstest.component.ImageLoader;
import com.wsy.geeknewstest.model.bean.news.NewsSummaryBean;
import com.wsy.geeknewstest.util.SystemUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2017/1/30.
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM = 1;
    public static final int TYPE_PHOTO_ITEM = 2;

    private List<NewsSummaryBean> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public NewsListAdapter(Context mContext, List<NewsSummaryBean> mList) {
        inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(mList.get(position).getDigest())) {
            return TYPE_ITEM;
        } else {
            return TYPE_PHOTO_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ITEM:
                view = inflater.inflate(R.layout.item_news, parent, false);
                ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder, false);
                return itemViewHolder;
            case TYPE_PHOTO_ITEM:
                view = inflater.inflate(R.layout.item_news_photo, parent, false);
                PhotoViewHolder photoItemViewHolder = new PhotoViewHolder(view);
                setItemOnClickEvent(photoItemViewHolder, true);
                return photoItemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItemValues((ItemViewHolder) holder, position);
        } else if (holder instanceof PhotoViewHolder) {
            setPhotoItemValues((PhotoViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder, final boolean isPhoto) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition(), isPhoto);
                }
            });
        }
    }

    private void setItemValues(ItemViewHolder holder, int position) {
        NewsSummaryBean newsSummary = mList.get(position);
        String title = newsSummary.getTitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        String time = newsSummary.getPtime();
        String digest = newsSummary.getDigest();
        String imgSrc = newsSummary.getImgsrc();

        holder.mNewsSummaryTitleTv.setText(title);
        holder.mNewsSummaryPtimeTv.setText(time);
        holder.mNewsSummaryDigestTv.setText(digest);

        ImageLoader.load(mContext, imgSrc, holder.mNewsSummaryPhotoIv);
    }


    private void setPhotoItemValues(PhotoViewHolder holder, int position) {
        NewsSummaryBean newsSummary = mList.get(position);
        String title = newsSummary.getTitle();
        String time = newsSummary.getPtime();

        holder.mNewsSummaryTitleTv.setText(title);
        holder.mNewsSummaryPtimeTv.setText(time);

        int PhotoOneHeight = SystemUtil.dp2px(mContext, 150);
        int PhotoTwoHeight = SystemUtil.dp2px(mContext, 120);
        int PhotoThreeHeight = SystemUtil.dp2px(mContext, 90);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.mNewsSummaryPhotoIvGroup.getLayoutParams();

        if (newsSummary.getAds() != null) {
            List<NewsSummaryBean.AdsBean> adsBeanList = newsSummary.getAds();
            int size = adsBeanList.size();
            if (size >= 3) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();
                imgSrcRight = adsBeanList.get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;

                holder.mNewsSummaryTitleTv.setText(String.format("图集：%1$s", adsBeanList.get(0).getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else if (newsSummary.getImgextra() != null) {
            int size = newsSummary.getImgextra().size();
            if (size >= 3) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();
                imgSrcRight = newsSummary.getImgextra().get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;
            } else if (size >= 2) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else {
            imgSrcLeft = newsSummary.getImgsrc();

            layoutParams.height = PhotoOneHeight;
        }

        setPhotoImageView(holder, imgSrcLeft, imgSrcMiddle, imgSrcRight);
        holder.mNewsSummaryPhotoIvGroup.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(PhotoViewHolder holder, String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvLeft, imgSrcLeft);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvLeft);
        }

        if (imgSrcMiddle != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvMiddle, imgSrcMiddle);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvMiddle);
        }

        if (imgSrcRight != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvRight, imgSrcRight);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvRight);
        }

    }

    private void showAndSetPhoto(ImageView imageView, String imgSrc) {
        imageView.setVisibility(View.VISIBLE);
        ImageLoader.load(mContext, imgSrc, imageView);
    }

    private void hidePhoto(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_summary_photo_iv)
        ImageView mNewsSummaryPhotoIv;
        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_digest_tv)
        TextView mNewsSummaryDigestTv;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_photo_iv_group)
        LinearLayout mNewsSummaryPhotoIvGroup;
        @BindView(R.id.news_summary_photo_iv_left)
        ImageView mNewsSummaryPhotoIvLeft;
        @BindView(R.id.news_summary_photo_iv_middle)
        ImageView mNewsSummaryPhotoIvMiddle;
        @BindView(R.id.news_summary_photo_iv_right)
        ImageView mNewsSummaryPhotoIvRight;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    public void updateData(List<NewsSummaryBean> list) {
        mList = list;
    }

    public List<NewsSummaryBean> getList() {
        return mList;
    }

    public void addMore(List<NewsSummaryBean> list) {
        int startPosition = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(startPosition, mList.size());
    }


}
