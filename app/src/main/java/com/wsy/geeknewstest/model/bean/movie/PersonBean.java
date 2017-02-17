package com.wsy.geeknewstest.model.bean.movie;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hasee on 2017/2/15.
 */

public class PersonBean implements Parcelable {
    /**
     * alt : https://movie.douban.com/celebrity/1050059/
     * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/1691.jpg","large":"https://img3.doubanio.com/img/celebrity/large/1691.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/1691.jpg"}
     * name : 范冰冰  or
     * name : 冯小刚
     * id : 1050059
     * type: 导演 或 演员
     */
    private String alt;
    // 导演或演员
    private String type;
    private ImagesBean avatars;
    private String name;
    private String id;

    public String getAlt() {
        return alt;
    }

    public ImagesBean getAvatars() {
        return avatars;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setAvatars(ImagesBean avatars) {
        this.avatars = avatars;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected PersonBean(Parcel in) {
        alt = in.readString();
        type = in.readString();
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<PersonBean> CREATOR = new Creator<PersonBean>() {
        @Override
        public PersonBean createFromParcel(Parcel in) {
            return new PersonBean(in);
        }

        @Override
        public PersonBean[] newArray(int size) {
            return new PersonBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alt);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(id);
    }
}
