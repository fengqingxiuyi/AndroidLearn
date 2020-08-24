package com.example.webview.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.example.utils.AppUtil;
import com.example.utils.LogUtil;

import java.util.List;

public class WebviewUtil {

    private static String PROCESS_NAME = "com.example.webview:webview";

    private static String sUserAgent;

    public static void initUserAgent(String userAgent) {
        sUserAgent = userAgent;
    }

    public static String getUserAgent() {
        return sUserAgent;
    }

    private static List<String> sRouterUrlList;

    public static void initRouterUrlList(List<String> routerUrlList) {
        sRouterUrlList = routerUrlList;
    }

    public static boolean isRouterUrl(String url) {
        if (sRouterUrlList == null || sRouterUrlList.isEmpty() || TextUtils.isEmpty(url)) {
            return false;
        }
        for (String str : sRouterUrlList) {
            if (url.contains(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWebviewProcess(Context context) {
        return AppUtil.INSTANCE.isMainProcess(context, PROCESS_NAME);
    }

    public static void killWebviewProcess(Context context) {
        int pid = -1;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) {
                return;
            }
            List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
                if (amProcess.processName.equals(PROCESS_NAME)) {
                    pid = amProcess.pid;
                    break;
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (pid != -1) {
            android.os.Process.killProcess(pid);
        }
    }

    public static void killWebviewProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
