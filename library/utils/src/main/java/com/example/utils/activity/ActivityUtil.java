package com.example.utils.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取指定包名下的Activity
 */
public class ActivityUtil {

    /**
     * 获取App中所有Activity的信息集合
     */
    public static List<ActivityInfo> getActivityInfos(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return Arrays.asList(packageInfo.activities);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App中所有Activity的名字集合
     */
    public static List<String> getActivityNames(Context context) {
        List<String> activityNames = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            final List<ActivityInfo> activityInfos = Arrays.asList(packageInfo.activities);
            if (activityInfos.size() < 1) {
                return activityNames;
            }
            // 将activityInfos中的name添加到集合activityNames中
            for (ActivityInfo activityInfo : activityInfos) {
                activityNames.add(activityInfo.name.substring(activityInfo.name.lastIndexOf(".") + 1));
            }
            return activityNames;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return activityNames;
    }

    /**
     * 设置Activity全屏
     */
    public static void setFullscreenMode(Activity activity) {
        if (activity != null) {
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}
