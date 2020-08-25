package com.example.learn

import android.app.Activity
import android.app.Application
import com.example.common.global.AppGlobal
import com.example.learn.koin.appModule
import com.example.learn.webview.JsInterfaceImpl
import com.example.ui.toast.ToastUtil
import com.example.utils.ActivitiesManager
import com.example.utils.AppLifecycleMonitor
import com.example.utils.AppUtil
import com.example.webview_module.js.JsBridge
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
        inAllProcess()
        if (AppUtil.isMainProcess(this)) {
            inMainProcess()
        } else {
            inOtherProcess()
        }
    }

    private fun inAllProcess() {
        initAppGlobal()
        initToast()
    }

    private fun inMainProcess() {
        initWebview()
        initKoin()
    }

    private fun inOtherProcess() {

    }

    private fun initAppGlobal() {
        AppGlobal.application = this
        AppGlobal.appContext = applicationContext
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
        JsBridge.get().register(JsInterfaceImpl::class.java)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}