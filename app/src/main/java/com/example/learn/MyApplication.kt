package com.example.learn

import android.app.Activity
import android.app.Application
import com.example.common.UserAgent
import com.example.common.global.AppGlobal
import com.example.learn.koin.appModule
import com.example.learn.webview.JsInterfaceImpl
import com.example.ui.toast.ToastUtil
import com.example.utils.ActivitiesManager
import com.example.utils.AppLifecycleMonitor
import com.example.utils.AppUtil
import com.example.webview.js.JsBridge
import com.example.webview.utils.WebviewUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author fqxyi
 * @date 2020/8/13
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppGlobal.application = this
        AppGlobal.appContext = applicationContext
        initLifecycleListener()
        initToast()
        initWebview()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    private fun initLifecycleListener() {
        AppLifecycleMonitor().setForegroundListener {
            AppGlobal.appForeground = it
        }
    }

    private fun initToast() {
        ToastUtil.init(object : ToastUtil.ToastListener {
            override fun getLastAliveActivity(): Activity {
                return ActivitiesManager.getInstance().lastAliveActivity
            }

            override fun isForeground(): Boolean {
                return AppGlobal.appForeground
            }
        })
    }

    private fun initWebview() {
        if (AppUtil.isMainProcess(this)) {
            JsBridge.get().register(JsInterfaceImpl::class.java)
        } else if (WebviewUtil.isWebviewProcess(this)) {
            WebviewUtil.initUserAgent(UserAgent.getUserAgent(this))
            WebviewUtil.initRouterUrlList(arrayListOf("debug.com/routes", "release.com/routes"))
        }
    }
}