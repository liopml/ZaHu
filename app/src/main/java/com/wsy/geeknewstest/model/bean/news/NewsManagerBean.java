package com.wsy.geeknewstest.model.bean.news;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by hasee on 2017/1/17.
 */

public class NewsManagerBean extends RealmObject implements Parcelable {

    private RealmList<NewsChannelTableBean> managerList;

    public NewsManagerBean() {

    }

    public NewsManagerBean(RealmList<NewsChannelTableBean> managerList) {
        this.managerList = managerList;
    }

    public RealmList<NewsChannelTableBean> getManagerList() {
        return managerList;
    }

    public void setManagerList(RealmList<NewsChannelTableBean> managerList) {
        this.managerList = managerList;
    }

    protected NewsManagerBean(Parcel in) {
        this.managerList = new RealmList<>();
        in.readList(this.managerList, NewsChannelTableBean.class.getClassLoader());
    }

    public static final Creator<NewsManagerBean> CREATOR = new Creator<NewsManagerBean>() {
        @Override
        public NewsManagerBean createFromParcel(Parcel in) {
            return new NewsManagerBean(in);
        }

        @Override
        public NewsManagerBean[] newArray(int size) {
            return new NewsManagerBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.managerList);
    }
}
