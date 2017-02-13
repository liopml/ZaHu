package com.wsy.geeknewstest.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hasee on 2016/11/20.
 */

public class ReadStateBean extends RealmObject{

    @PrimaryKey
    private int id;

    public ReadStateBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
