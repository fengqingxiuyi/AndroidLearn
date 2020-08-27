package com.example.common.base

import android.content.Context
import android.os.Bundle
import com.example.network.RequestManager
import com.example.shake.ShakeSensorManager
import com.example.ui.toast.ToastUtil
import com.example.utils.ActivitiesManager
import com.example.utils.permission.CheckPermissionActivity

/**
 * @author fqxyi
 * @date 2020/8/19
 * 业务基类
 */
open class BaseActivity : CheckPermissionActivity() {

    lateinit var activity: BaseActivity
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        context = applicationContext
        ActivitiesManager.getInstance().addActivity(activity)
    }

    override fun onResume() {
        super.onResume()
        ShakeSensorManager.getInstance().onActivityResumed(activity)
    }

    override fun onPause() {
        super.onPause()
        ShakeSensorManager.getInstance().onActivityPaused()
    }

    override fun onDestroy() {
        ActivitiesManager.getInstance().remove(activity)
        ToastUtil.onDestroy(activity)
        RequestManager.get().destroy(activity)
        super.onDestroy()
    }

}