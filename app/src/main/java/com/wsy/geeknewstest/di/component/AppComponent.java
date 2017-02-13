package com.wsy.geeknewstest.di.component;

import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.di.ContextLife;
import com.wsy.geeknewstest.di.module.AppModule;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hasee on 2016/11/8.
 * 用@Component表示这个接口是一个连接器，能用@Component注解的只能是interface或者抽象类
 */

@Singleton
//这里表示Component会从AppModule类中拿那些用@Provides注解的方法来生成需要注入的实例
@Component(modules = AppModule.class)
public interface AppComponent {

    @ContextLife("Application")
    App getContext();  // 提供App的Context

    RetrofitHelper retrofitHelper(); //提供http的帮助类

    RealmHelper realmHelper();    //提供数据库帮助类
}
