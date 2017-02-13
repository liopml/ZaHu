package com.wsy.geeknewstest.component;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.util.LogUtil;
import com.wsy.geeknewstest.util.ToastUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by hasee on 2016/11/6.
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static Thread.UncaughtExceptionHandler defaultHandler = null;

    private Context context = null;

    private final String TAG = CrashHandler.class.getSimpleName();

    public CrashHandler(Context context) {
        this.context = context;
    }

    /**
     * 初始化,设置该CrashHandler为程序的默认处理器
     */
    public static void init(CrashHandler crashHandler) {
        //获取系统默认的UncaughtException处理器
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println(ex.toString());
        LogUtil.e(TAG, ex.toString());
        LogUtil.e(TAG, collectCrashDeviceInfo());
        LogUtil.e(TAG, getCrashInfo(ex));
        // 调用系统错误机制
        defaultHandler.uncaughtException(thread, ex);
        ToastUtil.shortShow("抱歉,程序发生异常即将退出");
        App.getInstance().exitApp();
    }

    /**
     * 得到程序崩溃的详细信息
     */
    public String getCrashInfo(Throwable ex) {
        Writer result = new StringWriter();
        //创建不带自动行刷新的新 PrintWriter
        PrintWriter printWriter = new PrintWriter(result);
        // 设置将由 getStackTrace() 返回，并由 printStackTrace() 和相关方法输出的堆栈跟踪元素。
        ex.setStackTrace(ex.getStackTrace());
        //将此 throwable 及其追踪输出到指定的 PrintWriter。
        ex.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * 收集程序崩溃的设备信息
     */
    public String collectCrashDeviceInfo() {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = pi.versionName;  //版本号
            String model = android.os.Build.MODEL;  //获取手机的型号 设备名称。
            String androidVersion = android.os.Build.VERSION.RELEASE;  //获取系统版本字符串。如4.1.2 或2.2 或2.3等
            String manufacturer = android.os.Build.MANUFACTURER;  //获取设备制造商
            return versionName + "  " + model + "  " + androidVersion + "  " + manufacturer;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
