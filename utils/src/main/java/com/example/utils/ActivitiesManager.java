package com.example.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

public class ActivitiesManager {

    public static Stack<Activity> activityStack;
    private volatile static ActivitiesManager instance;

    private ActivitiesManager() {
    }

    /**
     * 单一实例
     */
    public static ActivitiesManager getInstance() {
        if (instance == null) {
            synchronized (ActivitiesManager.class) {
                if (instance == null) {
                    instance = new ActivitiesManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void remove(Activity activity) {
        try {
            if (activityStack == null) {
                return;
            }
            if (activityStack.size() == 0) {
                return;
            }
            activityStack.remove(activity);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack == null) {
            return null;
        }
        if (activityStack.size() == 0) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 获取最后一个活着的Activity
     */
    public Activity getLastAliveActivity() {
        if (activityStack == null) {
            return null;
        }
        if (activityStack.size() == 0) {
            return null;
        }
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (null != activityStack.get(i) && !activityStack.get(i).isFinishing()) {
                return activityStack.get(i);
            }
        }
        return null;
    }

    public String getObjectSimpleName(Object object) {
        String name = null;
        try {
            name = object.getClass().getSimpleName();
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return name;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (activityStack.size() == 0) {
            return;
        }

        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null) {
            return;
        }
        if (activityStack.size() == 0) {
            return;
        }

        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }
        if (activityStack.size() == 0) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivityByName(Activity object) {
        if (activityStack == null) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && activityStack.get(i) != object) {
                activityStack.get(i).finish();
            }
        }
    }

    /*** 是否包含 某一个 界面 ***/
    public boolean isExistByClass(Class object) {
        if (activityStack == null) {
            return false;
        }
        if (activityStack.size() == 0) {
            return false;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 彻底退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

}