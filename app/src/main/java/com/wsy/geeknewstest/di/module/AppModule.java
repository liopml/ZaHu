package com.wsy.geeknewstest.di.module;

import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.di.ContextLife;
import com.wsy.geeknewstest.model.db.RealmHelper;
import com.wsy.geeknewstest.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hasee on 2016/11/8.
 *
 * @Module注解表示这个类提供生成一些实例用于注入
 */

@Module
public class AppModule {

    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    /**
     * @return 返回注入对象
     * @Provides 注解表示这个方法是用来创建某个实例对象的，这里我们创建返回application对象
     * 方法名随便，一般用provideXXX结构
     */
    @Provides
    @Singleton
    @ContextLife("Application")
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    RetrofitHelper provideRetrofitHelper() {
        return new RetrofitHelper();
    }

    @Provides
    @Singleton
    RealmHelper provideRealmHelper() {
        return new RealmHelper();
    }
}
