package com.example.learn.iconchange;

import android.content.Context;

import com.example.common.BuildConfig;
import com.example.utils.iconchange.IconChangeUtil;

import static com.example.learn.iconchange.IconChangeConstant.ACTIVITY_PATH_ARR;

/**
 * @author fqxyi
 *
 * 动态替换ICON管理类
 *
 * 情景分析：
 *
 * 1、预埋一个ICON，需要执行以下两步：
 * 1.1、修改AndroidManifest.xml文件中activity-alias的android:icon值
 * 1.2、替换{@link #getActivityPath}方法中iconType的case值
 *
 * 2、预埋两个ICON，需要执行以下几步：
 * 2.1、修改AndroidManifest.xml文件中activity-alias的android:icon值，并新增一个activity-alias
 * 2.2、为{@link ACTIVITY_PATH_ARR}添加新增activity-alias的name，name名记得写的通用些
 * 2.3、替换getActivityPath方法中iconType的case值，并新增一个case
 *
 * 3、预埋多个ICON，同理预埋两个ICON，但是需要注意：一旦新增过activity-alias就不能再删除，
 * 否则可能存在应用升级之后，在桌面上找不到应用的情况。
 */
public class IconChangeManager {

    /**
     * 此方法会频繁变更，所以放在最前面
     */
    private static String getActivityPath(int iconType) {
        String activityPath = ACTIVITY_PATH_ARR[0];
        switch (iconType) {
            case IconChangeConstant.CHANGE: // 对应第一个预埋icon
                activityPath = ACTIVITY_PATH_ARR[1];
                break;
            case IconChangeConstant.CHANGE2: // 对应第二个预埋icon
                activityPath = ACTIVITY_PATH_ARR[2];
                break;
        }
        return activityPath;
    }

    /**
     * 更换应用icon
     * 注意：只能在后台更换应用ICON，否则会出现闪烁等奇怪的问题
     */
    public static void changeIcon(Context context, int iconType) {
        if (context == null || BuildConfig.DEBUG) {
            return;
        }
        if (iconType == IconChangeConstant.NORMAL) { // 不更新icon，需要重置为默认icon
            String activityPath = ACTIVITY_PATH_ARR[0];
            if (!IconChangeUtil.componentEnabled(context, activityPath)) {
                IconChangeUtil.enableComponent(context, activityPath, ACTIVITY_PATH_ARR);
            }
            return;
        }
        IconChangeUtil.enableComponent(context, getActivityPath(iconType), ACTIVITY_PATH_ARR);
    }

}
