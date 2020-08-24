package com.example.webview.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.example.webview.IWebviewBinder;
import com.example.webview.IWebviewBinderCallback;
import com.example.webview.business.router.WebviewRouter;

/**
 * 主进程中的Service
 */
public class MainRemoteService extends Service {

    private final int CODE_HANDLE_JS_FUNC = 1;

    private ServiceHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new ServiceHandler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    /**
     * handleJsFunc函数运行在主进程的Binder线程中
     */
    private IWebviewBinder.Stub serviceBinder = new IWebviewBinder.Stub() {

        @Override
        public void handleJsFunc(String url, IWebviewBinderCallback callback) throws RemoteException {
            if (handler != null) {
                Message message = Message.obtain();
                message.what = CODE_HANDLE_JS_FUNC;
                message.obj = callback;
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }

    };

    /**
     * handleMessage函数运行在主进程的主线程中
     */
    private class ServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_HANDLE_JS_FUNC:
                    Bundle bundle = msg.getData();
                    WebviewRouter.h5ToNative(getApplicationContext(),
                            bundle.getString("url"),
                            (IWebviewBinderCallback) msg.obj);
                    break;
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
