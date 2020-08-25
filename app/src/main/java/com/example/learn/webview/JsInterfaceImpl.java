package com.example.learn.webview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.example.learn.BuildConfig;
import com.example.ui.toast.ToastUtil;
import com.example.utils.ActivitiesManager;
import com.example.webview_module.IWebviewBinderCallback;
import com.example.webview_module.js.JsBridge;

/**
 * JS交互实现类，在{@link JsBridge}类中注册
 *
 * 重点：
 * 一般情况下Context均为Application，所以在执行跳转页面等必须要用Activity的时候，请使用以下语句：
 * ActivitiesManager.getInstance().getLastAliveActivity()
 */
public class JsInterfaceImpl {

    /**
     * 因为Webview处于独立进程中，所以需要通过以下方式获取上一个Activity
     */
    private Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity)context;
        } else {
            return ActivitiesManager.getInstance().getLastAliveActivity();
        }
    }

    public void switchApi(Context context, String params, IWebviewBinderCallback callback) {
        if (checkContextAndParamsError(context, params)) {
            return;
        }
        //拿到params之后执行切换API的业务逻辑
    }

    /***                               提供给本类的工具方法                                 ***/

    private static long lastClickTime;

    private boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (Math.abs(time - lastClickTime) < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private boolean checkContextAndParamsError(Context context, String params) {
        return checkContextError(context) || checkParamsError(params);
    }

    private boolean checkContextError(Context context) {
        if (context instanceof Activity) {
            return false;
        }
        toast("context 不是 activity");
        return true;
    }

    private boolean checkParamsError(String params) {
        if (!TextUtils.isEmpty(params)) {
            return false;
        }
        toast("params为空");
        return true;
    }

    /**
     * 仅DEBUG包弹toast提示
     */
    private void toast(String msg) {
        if (BuildConfig.DEBUG) {
            ToastUtil.toast(msg);
        }
    }

}
