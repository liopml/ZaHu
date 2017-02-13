package com.wsy.geeknewstest.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.wsy.geeknewstest.component.CrashHandler;
import com.wsy.geeknewstest.di.component.AppComponent;
import com.wsy.geeknewstest.di.component.DaggerAppComponent;
import com.wsy.geeknewstest.di.module.AppModule;
import com.wsy.geeknewstest.widget.AppBlockCanaryContext;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;


/**
 * Created by hasee on 2016/11/6.
 */

public class App extends Application {

    private static App instance;
    private Set<Activity> allActivities;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    public static synchronized App getInstance() {
        return instance;
    }

    static {
        //MODE_NIGHT_NO： 使用亮色(light)主题，不使用夜间模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化屏幕宽高
        getScreenSize();

        //初始化日志
        Logger.init(getPackageName()).hideThreadInfo();// 隐藏线程信息

        //初始化错误收集
        CrashHandler.init(new CrashHandler(getApplicationContext()));

        //初始化内存泄漏检测
        LeakCanary.install(this);

        //初始化过度绘制检测
        BlockCanary.install(this, new AppBlockCanaryContext()).start();

        //初始化tbs x5 webview
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });

        //初始化Realm
        Realm.init(this);

    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<Activity>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        //打开的窗口程序
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        //获取手机屏幕分辨率的类
        DisplayMetrics dm = new DisplayMetrics();
        //获取默认显示的 Display 对象。
        Display dispaly = windowManager.getDefaultDisplay();
        //把屏幕尺寸信息赋值给DisplayMetrics
        dispaly.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;  //屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        DIMEN_DPI = dm.densityDpi;  //屏幕密度（每寸像素：120/160/240/320）
        SCREEN_WIDTH = dm.widthPixels;  //屏幕宽（像素，如：480px）
        SCREEN_HEIGHT = dm.heightPixels;   // 屏幕高（像素，如：800px）
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    // 使用Dagger2生成的类 生成组件进行构造，并注入
    public static AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }
}
