package com.example.learn

import android.app.Application
import com.example.common.global.AppGlobal
import com.example.learn.koin.appModule
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
        //
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}