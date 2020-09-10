package com.example.webview_module.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.utils.LogUtil;
import com.example.utils.app.AppUtil;

import java.util.List;

public class WebviewUtil {

    private static String PROCESS_NAME = "com.example.webview:webview";

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
