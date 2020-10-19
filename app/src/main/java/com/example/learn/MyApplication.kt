package com.example.learn

import android.app.Activity
import android.app.Application
import com.example.common.global.AppGlobal
import com.example.common.network.API
import com.example.common.network.BusinessObserver
import com.example.common.network.interceptor.HeaderInterceptor
import com.example.image.fresco.FrescoImageView
import com.example.learn.iconchange.IconChangeConstant
import com.example.learn.iconchange.IconChangeManager
import com.example.learn.koin.appModule
import com.example.learn.ui.webview.JsInterfaceImpl
import com.example.network.RequestManager
import com.example.network.callback.IInterceptorCallback
import com.example.network.callback.IObserverCallback
import com.example.network.callback.IResponseCallback
import com.example.network.interceptor.FormToJsonInterceptor
import com.example.network.interceptor.HttpCacheInterceptor
import com.example.network.observer.IBaseObserver
import com.example.network.tag.ReqTag
import com.example.social.SocialHelper
import com.example.ui.toast.ToastUtil
import com.example.utils.activity.ActivitiesManager
import com.example.utils.app.AppLifecycleMonitor
import com.example.utils.app.AppUtil
import com.example.utils.storage.StorageManagerUtil
import com.example.webview_module.js.JsBridge
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
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
        initStorage()
        initToast()
    }

    private fun inMainProcess() {
        //Debug Release Special Compile
        Config.setting(this)
        initWebview()
        initKoin()
        initNet()
        initSocial()
        initImage()
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

    private fun initStorage() {
        StorageManagerUtil.init(this)
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
            /**
             * version: kotlin: 1.4.0 , koin: 2.1.6
             * add Level.ERROR for fix error:
             * java.lang.NoSuchMethodError: No virtual method elapsedNow()D in class Lkotlin/time/TimeMark;
             * or its super classes (declaration of 'kotlin.time.TimeMark' appears in /data/app/com.example.learn-2/base.apk)
             * link: https://github.com/InsertKoinIO/koin/issues/847
             */
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    private fun initNet() {
        RequestManager.get().init(AppGlobal.application, API.API_BASE,
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
                    interceptorList.add(HeaderInterceptor(this@MyApplication))
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

    private fun initSocial() {
        //初始化数据
        SocialHelper.get().setQqAppId("1107009250")
            .setWxAppId("wx2847b18acb41e535")
            .setWxAppSecret("78f713b76c61a38242e63ccdb3a96d68")
            .setWbAppId("2214687859")
            .setWbRedirectUrl("https://github.com/fengqingxiuyi")
    }

    private fun initImage() {
        FrescoImageView.init(this)
    }
}