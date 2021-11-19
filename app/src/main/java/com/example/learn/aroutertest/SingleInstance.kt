package com.example.learn.aroutertest

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.utils.LogUtil

/**
 * ARouter不支持这种形式，会抛出异常： Caused by: com.alibaba.android.arouter.exception.HandlerException: Init provider failed! <init> []
 */
@Route(path = "/app/single")
class SingleInstance private constructor(private val str: String) : IProviderTest {

    companion object {
        @Volatile
        private var instance: SingleInstance? = null

        fun getInstance(str: String): SingleInstance {
            if (instance == null) {
                synchronized(SingleInstance::class.java) {
                    if (instance == null) {
                        instance =
                            SingleInstance(str)
                    }
                }
            }
            return instance!!
        }
    }

    var testProvider: Int = 0

    fun single() {
        LogUtil.i("kotlin class SingleInstance $str companion object single $testProvider")
    }

    override fun testSingleInstance() {
        testProvider = 2
    }

    override fun init(context: Context?) {

    }


}