package com.wsy.geeknewstest.model.bean.gold;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by hasee on 2016/12/11.
 */

public class GoldManagerItemBean extends RealmObject implements Parcelable {

    private int index;

    private boolean isSelect;

    public GoldManagerItemBean() {

    }

    public GoldManagerItemBean(int index, boolean isSelect) {
        this.index = index;
        this.isSelect = isSelect;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    // 系统自动添加，给createFromParcel里面用
    protected GoldManagerItemBean(Parcel in) {
        this.index = in.readInt();
        this.isSelect = in.readByte() != 0;
    }

    // 内容接口描述，默认返回0即可。
    @Override
    public int describeContents() {
        return 0;
    }

    //写入接口函数，打包
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    /*
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     * 因为实现类在这里还是不可知的，所以需要用到模板的方式，继承类名通过模板参数传入。
     * 为了能够实现模板参数的传入，这里定义Creator嵌入接口,内含两个接口函数分别返回单个和多个继承类实例。
     */
    public static final Creator<GoldManagerItemBean> CREATOR = new Creator<GoldManagerItemBean>() {

        /**
         * createFromParcel()方法中我们要去读取刚才写出的name和age字段，
         * 并创建一个Person对象进行返回，其中color和size都是调用Parcel的readXxx()方法读取到的，
         * 注意这里读取的顺序一定要和刚才写出的顺序完全相同。
         * 读取的工作我们利用一个构造函数帮我们完成了
         */
        @Override
        public GoldManagerItemBean createFromParcel(Parcel in) {
            return new GoldManagerItemBean(in);
        }

        //供反序列化本类数组时调用的
        @Override
        public GoldManagerItemBean[] newArray(int size) {
            return new GoldManagerItemBean[size];
        }
    };
}
