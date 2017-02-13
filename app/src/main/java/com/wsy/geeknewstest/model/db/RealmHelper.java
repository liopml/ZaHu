package com.wsy.geeknewstest.model.db;

import android.content.Context;

import com.wsy.geeknewstest.model.bean.ReadStateBean;
import com.wsy.geeknewstest.model.bean.RealmLikeBean;
import com.wsy.geeknewstest.model.bean.gold.GoldManagerBean;
import com.wsy.geeknewstest.model.bean.news.NewsManagerBean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by hasee on 2016/11/20.
 */

public class RealmHelper {

    private static final String DB_NAME = "myRealm.realm";

    private Realm mRealm;

    public RealmHelper() {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DB_NAME)
                .build());
    }

    /**
     * 增加 阅读记录
     * 使用@PrimaryKey注解后copyToRealm需要替换为copyToRealmOrUpdate
     */
    public void insertNewsId(int id) {
        ReadStateBean bean = new ReadStateBean();
        bean.setId(id);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    /**
     * 查询 阅读记录
     */
    public boolean queryNewsId(int id) {
        //查询全部
        RealmResults<ReadStateBean> results = mRealm.where(ReadStateBean.class).findAll();
        for (ReadStateBean item : results) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * 增加 收藏记录
     */
    public void insertLikeBean(RealmLikeBean bean) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);  //添加
        mRealm.commitTransaction();
    }


    /**
     * 删除 收藏记录
     */
    public void deleteLikeBean(String id) {
        //查询条件为id
        RealmLikeBean data = mRealm.where(RealmLikeBean.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        if (data != null) {
            data.deleteFromRealm();
        }
        mRealm.commitTransaction();
    }

    /**
     * 查询 收藏记录
     */
    public boolean queryLikeId(String id) {
        RealmResults<RealmLikeBean> results = mRealm.where(RealmLikeBean.class).findAll();
        for (RealmLikeBean item : results) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public List<RealmLikeBean> getLikeList() {
        //使用findAllSort ,先findAll再result.sort无效
        RealmResults<RealmLikeBean> results = mRealm.where(RealmLikeBean.class).findAllSorted("time");
        //copyFromRealm方法将它转为List<T>
        return mRealm.copyFromRealm(results);
    }

    /**
     * 修改 收藏记录 时间戳以重新排序
     */
    public void changeLikeTime(String id, long time, boolean isPlus) {
        RealmLikeBean bean = mRealm.where(RealmLikeBean.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        if (isPlus) {
            bean.setTime(++time);
        } else {
            bean.setTime(--time);
        }
        mRealm.commitTransaction();
    }

    /**
     * 更新 掘金首页管理列表
     */
    public void updateGoldManagerList(GoldManagerBean bean) {
        GoldManagerBean data = mRealm.where(GoldManagerBean.class).findFirst();
        mRealm.beginTransaction();
        if (data != null) {
            data.deleteFromRealm();
        }
        mRealm.copyToRealm(bean);
        mRealm.commitTransaction();
    }

    /**
     * 获取 掘金首页管理列表
     */
    public GoldManagerBean getGoldManagerList() {
        GoldManagerBean bean = mRealm.where(GoldManagerBean.class).findFirst();
        if (bean == null) {
            return null;
        }
        return mRealm.copyFromRealm(bean);
    }

    /**
     * 更新 新闻首页管理列表
     */
    public void updateNewsManagerList(NewsManagerBean bean) {
        NewsManagerBean data = mRealm.where(NewsManagerBean.class).findFirst();
        mRealm.beginTransaction();
        if (data != null) {
            data.deleteFromRealm();
        }
        mRealm.copyToRealm(bean);
        mRealm.commitTransaction();
    }

    /**
     * 获取 新闻首页管理列表
     */
    public NewsManagerBean getNewsManagerBean() {
        NewsManagerBean bean = mRealm.where(NewsManagerBean.class).findFirst();
        if (bean == null) {
            return null;
        }
        return mRealm.copyFromRealm(bean);
    }


}
