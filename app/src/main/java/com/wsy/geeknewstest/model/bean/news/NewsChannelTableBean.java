package com.wsy.geeknewstest.model.bean.news;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hasee on 2016/12/22.
 */

public class NewsChannelTableBean extends RealmObject implements Parcelable {

    private String newsChannelName;     //频道名称
    private String newsChannelId;       //频道ID
    private String newsChannelType;     //频道类型
    private boolean newsChannelSelect;  //频道是否选中
    private int newsChannelIndex;       //频道排序的位置
    private boolean newsChannelFixed;   //频道是否固定

    public NewsChannelTableBean() {

    }

    public NewsChannelTableBean(String newsChannelName, String newsChannelId, String newsChannelType, boolean newsChannelSelect, int newsChannelIndex, boolean newsChannelFixed) {
        this.newsChannelName = newsChannelName;
        this.newsChannelId = newsChannelId;
        this.newsChannelType = newsChannelType;
        this.newsChannelSelect = newsChannelSelect;
        this.newsChannelIndex = newsChannelIndex;
        this.newsChannelFixed = newsChannelFixed;
    }

    public String getNewsChannelName() {
        return newsChannelName;
    }

    public void setNewsChannelName(String newsChannelName) {
        this.newsChannelName = newsChannelName;
    }

    public String getNewsChannelId() {
        return newsChannelId;
    }

    public void setNewsChannelId(String newsChannelId) {
        this.newsChannelId = newsChannelId;
    }

    public String getNewsChannelType() {
        return newsChannelType;
    }

    public void setNewsChannelType(String newsChannelType) {
        this.newsChannelType = newsChannelType;
    }

    public boolean isNewsChannelSelect() {
        return newsChannelSelect;
    }

    public void setNewsChannelSelect(boolean newsChannelSelect) {
        this.newsChannelSelect = newsChannelSelect;
    }

    public int getNewsChannelIndex() {
        return newsChannelIndex;
    }

    public void setNewsChannelIndex(int newsChannelIndex) {
        this.newsChannelIndex = newsChannelIndex;
    }

    public boolean isNewsChannelFixed() {
        return newsChannelFixed;
    }

    public void setNewsChannelFixed(boolean newsChannelFixed) {
        this.newsChannelFixed = newsChannelFixed;
    }

    // 系统自动添加，给createFromParcel里面用
    protected NewsChannelTableBean(Parcel in) {
        newsChannelName = in.readString();
        newsChannelId = in.readString();
        newsChannelType = in.readString();
        newsChannelSelect = in.readByte() != 0;
        newsChannelIndex = in.readInt();
        newsChannelFixed = in.readByte() != 0;
    }

    // 内容接口描述，默认返回0即可。
    @Override
    public int describeContents() {
        return 0;
    }

    //写入接口函数，打包
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsChannelName);
        dest.writeString(newsChannelId);
        dest.writeString(newsChannelType);
        dest.writeByte((byte) (newsChannelSelect ? 1 : 0));
        dest.writeInt(newsChannelIndex);
        dest.writeByte((byte) (newsChannelFixed ? 1 : 0));
    }

    /*
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     * 因为实现类在这里还是不可知的，所以需要用到模板的方式，继承类名通过模板参数传入。
     * 为了能够实现模板参数的传入，这里定义Creator嵌入接口,内含两个接口函数分别返回单个和多个继承类实例。
     */
    public static final Creator<NewsChannelTableBean> CREATOR = new Creator<NewsChannelTableBean>() {

        /**
         * createFromParcel()方法中我们要去读取刚才写出的name和age字段，
         * 并创建一个Person对象进行返回，其中color和size都是调用Parcel的readXxx()方法读取到的，
         * 注意这里读取的顺序一定要和刚才写出的顺序完全相同。
         * 读取的工作我们利用一个构造函数帮我们完成了
         */
        @Override
        public NewsChannelTableBean createFromParcel(Parcel in) {
            return new NewsChannelTableBean(in);
        }

        //供反序列化本类数组时调用的
        @Override
        public NewsChannelTableBean[] newArray(int size) {
            return new NewsChannelTableBean[size];
        }
    };


}
