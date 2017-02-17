package com.wsy.geeknewstest.model.bean.movie;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hasee on 2017/2/15.
 */

public class ImagesBean implements Parcelable {
    /**
     * small : https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p2378133884.jpg
     * large : https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2378133884.jpg
     * medium : https://img3.doubanio.com/view/movie_poster_cover/spst/public/p2378133884.jpg
     */
    private String small;
    private String large;
    private String medium;

    public String getSmall() {
        return small;
    }

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    protected ImagesBean(Parcel in) {
        small = in.readString();
        large = in.readString();
        medium = in.readString();
    }

    public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
        @Override
        public ImagesBean createFromParcel(Parcel in) {
            return new ImagesBean(in);
        }

        @Override
        public ImagesBean[] newArray(int size) {
            return new ImagesBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(large);
        dest.writeString(medium);
    }
}
