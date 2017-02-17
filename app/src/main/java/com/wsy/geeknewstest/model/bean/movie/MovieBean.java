package com.wsy.geeknewstest.model.bean.movie;

import java.util.List;

/**
 * Created by hasee on 2017/2/14.
 */

public class MovieBean {

    /**
     * title : 正在上映的电影-北京
     * total : 39
     * start : 0
     * count : 20
     * subjects : ["<Subject>","..."]
     */

    private String title;
    private int total;
    private int start;
    private int count;
    private List<SubjectsBean> subjects;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }
}
