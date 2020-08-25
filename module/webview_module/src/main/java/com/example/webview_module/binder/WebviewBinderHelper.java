package com.example.webview_module.binder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.utils.LogUtil;
import com.example.webview_module.IWebviewBinder;
import com.example.webview_module.IWebviewBinderCallback;
import com.example.webview_module.js.JsInterface;
import com.example.webview_module.service.MainRemoteService;

public class WebviewBinderHelper implements JsInterface.JsFuncCallback {

    private Activity activity;
    private WebviewBinderCallback callback;
    //
    private JsInterface jsInterface;
    //
    private IWebviewBinder iWebviewBinder;
    //
    private ServiceConnection conn;

    public WebviewBinderHelper(Activity activity, WebviewBinderCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void initJsInterface() {
        jsInterface = new JsInterface();
        jsInterface.setCallback(this);
    }

    public void bindMainRemoteService(Activity activity) {
        activity.bindService(
                new Intent(activity, MainRemoteService.class),
                conn = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        iWebviewBinder = IWebviewBinder.Stub.asInterface(iBinder);
                        if (callback != null) {
                            callback.onServiceConnected();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                },
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean execute(String url) {
        if (iWebviewBinder == null) return false;
        try {
            iWebviewBinder.handleJsFunc(url, new IWebviewBinderCallback.Stub() {
                @Override
                public void callJs(String params) throws RemoteException {
                    if (callback != null) {
                        callback.callJs(params);
                    }
                }
            });
            return true;
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return false;
    }

    public void destroyConnection() {
        if (conn != null) {
            activity.unbindService(conn);
        }
    }

    public JsInterface getJsInterface() {
        return jsInterface;
    }

}
