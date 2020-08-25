package com.example.utils.iconchange;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.example.utils.LogUtil;

/**
 * @author fqxyi
 * @date 2020/8/25
 * 应用ICON切换工具类
 */
public class IconChangeUtil {

    /**
     * 判断 component 是否可用
     */
    public static boolean componentEnabled(Context context, String activityPath) {
        if (context == null) {
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            ComponentName cn = new ComponentName(context, context.getPackageName() + activityPath);
            return pm.getComponentEnabledSetting(cn) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断 component 是否被禁用
     */
    public static boolean componentDisabled(Context context, String activityPath) {
        if (context == null) {
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            ComponentName cn = new ComponentName(context, context.getPackageName() + activityPath);
            return pm.getComponentEnabledSetting(cn) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 启用组件
     */
    public static void enableComponent(Context context, String activityPath, String[] ACTIVITY_PATH_ARR) {
        if (context == null || TextUtils.isEmpty(activityPath)) {
            return;
        }
        if (componentEnabled(context, activityPath)) {
            return;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            // 先禁用除需要启用组件之外的组件
            for (String item : ACTIVITY_PATH_ARR) {
                if (item.equals(activityPath)) {
                    continue;
                }
                packageManager.setComponentEnabledSetting(
                        new ComponentName(context, context.getPackageName() + item),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
            // 再启用需要启用的组件
            packageManager.setComponentEnabledSetting(
                    new ComponentName(context, context.getPackageName() + activityPath),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0); // 0表示立刻杀死应用，并刷新ICON
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开应用
     * @param uri intent 数据
     */
    public static void openApp(Context context, Uri uri, String[] ACTIVITY_PATH_ARR) {
        if (context == null) {
            return;
        }
        try {
            for (String item : ACTIVITY_PATH_ARR) {
                if (componentDisabled(context, item)) {
                    continue;
                }
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(
                        context, context.getPackageName() + item);
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(uri);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

}
