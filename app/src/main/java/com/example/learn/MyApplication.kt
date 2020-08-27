package com.example.learn

import android.app.Activity
import android.app.Application
import com.example.common.global.AppGlobal
import com.example.common.network.BusinessObserver
import com.example.learn.iconchange.IconChangeConstant
import com.example.learn.iconchange.IconChangeManager
import com.example.learn.koin.appModule
import com.example.learn.webview.JsInterfaceImpl
import com.example.network.RequestManager
import com.example.network.callback.IInterceptorCallback
import com.example.network.callback.IObserverCallback
import com.example.network.callback.IResponseCallback
import com.example.network.interceptor.FormToJsonInterceptor
import com.example.network.interceptor.HttpCacheInterceptor
import com.example.network.observer.IBaseObserver
import com.example.network.tag.ReqTag
import com.example.ui.toast.ToastUtil
import com.example.utils.ActivitiesManager
import com.example.utils.AppLifecycleMonitor
import com.example.utils.AppUtil
import com.example.webview_module.js.JsBridge
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*

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
        initNet()
    }

    private fun inOtherProcess() {

    }

    private fun initAppGlobal() {
        AppGlobal.application = this
        AppGlobal.appContext = applicationContext
        val appLifecycleMonitor = AppLifecycleMonitor()
        appLifecycleMonitor.setForegroundListener(object : AppLifecycleMonitor.ForegroundListener {
            override fun getForegroundStatus(foreground: Boolean) {
                AppGlobal.appForeground = foreground
                if (!foreground) { //处于后台状态
                    IconChangeManager.changeIcon(this@MyApplication, IconChangeConstant.CHANGE)
                }
            }
        })
        registerActivityLifecycleCallbacks(appLifecycleMonitor)
    }

    private fun initToast() {
        ToastUtil.init(object : ToastUtil.ToastListener {
            override fun getLastAliveActivity(): Activity? {
                return ActivitiesManager.getInstance().getLastAliveActivity()
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

    private fun initNet() {
        RequestManager.get().init(AppGlobal.application, "http://192.168.10.134:9900/",
            object : IObserverCallback {
                override fun <T : Any?> getObserver(
                    reqTag: ReqTag,
                    responseCallback: IResponseCallback<T>
                ): IBaseObserver<T> {
                    return BusinessObserver(
                        reqTag,
                        responseCallback
                    )
                }
            },
            object : IInterceptorCallback {
                override fun getInterceptorList(): MutableList<Interceptor> {
                    val interceptorList: MutableList<Interceptor> = ArrayList()
                    interceptorList.add(FormToJsonInterceptor())
                    return interceptorList
                }

                override fun getNetworkInterceptorList(): MutableList<Interceptor> {
                    val interceptorList: MutableList<Interceptor> = ArrayList()
                    interceptorList.add(HttpCacheInterceptor(this@MyApplication))
                    return interceptorList
                }
            })
    }
}