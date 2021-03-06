package com.wsy.geeknewstest.model.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hasee on 2016/11/20.
 * 用于数据库的bean 种类包括 新闻、技术、福利
 */

public class RealmLikeBean extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;

    private String image;

    private String title;

    private String url;

    private int type;

    private long time;

    public RealmLikeBean() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
