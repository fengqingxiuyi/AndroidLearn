package com.example.learn;

import android.app.Application;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;

import com.example.common.global.AppGlobal;
import com.example.learn.fps.Seat;
import com.example.learn.fps.Takt;
import com.example.log.LogUtil;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author fqxyi
 * @date 2017/12/11
 * Debug Release Special Compile
 */
public class Config {

    /**
     * 注释代码：本地手动打开，切勿提交
     */
    public static void setting(Application application) {
        try {
            //开启LeakCanary
            enableLeakCanary(application);
            //开启BlockCanary
            enableBlockCanary(application);
            //开启Takt
            enableTakt();
            //开启严格模式
            enableStrictMode();
        } catch (Throwable e) {
            LogUtil.e(e);
        }
    }

    private static void enableLeakCanary(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(application);
    }

    private static void enableBlockCanary(Application application) {
        BlockCanary.install(application, new AppBlockCanaryContext(application)).start();
    }

    private static void enableTakt() {
        Takt.stock(AppGlobal.application)
                .seat(Seat.BOTTOM_RIGHT)
                .interval(250)
                .color(Color.BLACK)
                .size(8f)
                .alpha(.5f)
                .useCustomControl();
    }

    /**
     * 严格模式，结果通过adb logcat | grep StrictMode查看
     */
    private static void enableStrictMode() {
        //线程策略检测
        StrictMode.ThreadPolicy.Builder threadBuilder = new StrictMode.ThreadPolicy.Builder();
        //磁盘读取操作
        threadBuilder.detectDiskReads();
        //磁盘写入操作
        threadBuilder.detectDiskWrites();
        //网络操作
        threadBuilder.detectNetwork();
        //自定义的耗时
        threadBuilder.detectCustomSlowCalls();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            threadBuilder.detectResourceMismatches();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            threadBuilder.detectUnbufferedIo();
        }
        StrictMode.setThreadPolicy(threadBuilder
                .penaltyLog()
                .build());
        //虚拟机策略检测
        StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder();
        //泄露的Sqlite对象
        vmBuilder.detectLeakedSqlLiteObjects();
        //Activity泄露
        vmBuilder.detectActivityLeaks();
        //未关闭的Closable对象泄露
        vmBuilder.detectLeakedClosableObjects();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            vmBuilder.detectLeakedRegistrationObjects();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //应用是否通过file:///的方式将文件共享给其他应用
            vmBuilder.detectFileUriExposure();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //应用是否使用SSL/TLS加密网络传输数据
//            vmBuilder.detectCleartextNetwork();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vmBuilder.detectContentUriWithoutPermission();
//            vmBuilder.detectUntaggedSockets();
        }
        StrictMode.setVmPolicy(vmBuilder
                .penaltyLog()
                .build());
    }

    /**
     * 清理资源
     */
    public static void destroy() {
        try {
            Takt.finish();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

}
