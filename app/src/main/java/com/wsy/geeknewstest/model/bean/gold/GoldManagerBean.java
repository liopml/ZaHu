package com.wsy.geeknewstest.model.bean.gold;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by hasee on 2016/12/11.
 */

public class GoldManagerBean extends RealmObject implements Parcelable {

    private RealmList<GoldManagerItemBean> managerList;

    public GoldManagerBean() {

    }

    public GoldManagerBean(RealmList<GoldManagerItemBean> mList) {
        this.managerList = mList;
    }

    public RealmList<GoldManagerItemBean> getManagerList() {
        return managerList;
    }

    public void setManagerList(RealmList<GoldManagerItemBean> managerList) {
        this.managerList = managerList;
    }

    // 内容接口描述，默认返回0即可。
    @Override
    public int describeContents() {
        return 0;
    }

    //写入接口函数，打包
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.managerList);
    }

    // 系统自动添加，给createFromParcel里面用
    protected GoldManagerBean(Parcel in) {
        this.managerList = new RealmList<>();
        in.readList(this.managerList, GoldManagerItemBean.class.getClassLoader());
    }

    /*
    * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
    * 因为实现类在这里还是不可知的，所以需要用到模板的方式，继承类名通过模板参数传入。
    * 为了能够实现模板参数的传入，这里定义Creator嵌入接口,内含两个接口函数分别返回单个和多个继承类实例。
    */
    public static final Creator<GoldManagerBean> CREATOR = new Creator<GoldManagerBean>() {

        /**
         * createFromParcel()方法中我们要去读取刚才写出的name和age字段，
         * 并创建一个Person对象进行返回，其中color和size都是调用Parcel的readXxx()方法读取到的，
         * 注意这里读取的顺序一定要和刚才写出的顺序完全相同。
         * 读取的工作我们利用一个构造函数帮我们完成了
         */
        @Override
        public GoldManagerBean createFromParcel(Parcel in) {
            return new GoldManagerBean(in);
        }

        //供反序列化本类数组时调用的
        @Override
        public GoldManagerBean[] newArray(int size) {
            return new GoldManagerBean[size];
        }
    };
}
