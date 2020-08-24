package com.example.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 监听APP生命周期变化
 */
public class AppLifecycleMonitor implements Application.ActivityLifecycleCallbacks {

    //前后台状态回调监听器
    private ForegroundListener foregroundListener;
    //计数器
    private int count = 0;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        synchronized (this) {
            if (count == 0) { //后台切换到前台
                if (foregroundListener != null) {
                    foregroundListener.getForegroundStatus(true);
                }
            }
            count++;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        synchronized (this) {
            count--;
            if (count == 0) { //前台切换到后台
                if (foregroundListener != null) {
                    foregroundListener.getForegroundStatus(false);
                }
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public void setForegroundListener(ForegroundListener foregroundListener) {
        this.foregroundListener = foregroundListener;
    }

    public interface ForegroundListener {
        void getForegroundStatus(boolean foreground);
    }

}
