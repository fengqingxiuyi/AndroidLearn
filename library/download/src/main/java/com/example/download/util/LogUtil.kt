package com.example.download.util

import android.util.Log
import com.example.download.BuildConfig

/**
 * @author fqxyi
 * @date 2018/4/21
 * 打印日志
 */
object LogUtil {

    private const val TAG = "DownloadDemo"

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg)
        }
    }

}