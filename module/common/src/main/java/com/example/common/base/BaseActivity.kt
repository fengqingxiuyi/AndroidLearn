package com.example.common.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.common.ui.titlebar.TitleBarView
import com.example.network.RequestManager
import com.example.shake.ShakeSensorManager
import com.example.ui.toast.ToastUtil
import com.example.utils.activity.ActivitiesManager
import com.example.utils.permission.CheckPermissionActivity
import io.reactivex.disposables.Disposable

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
        // TitleBarView
        val titleBarView = getTitleBarView()
        if (titleBarView != null && titleBarView.visibility == View.VISIBLE) {
            titleBarView.setLeftIconClickListener(View.OnClickListener { leftIconClickEvent() })
        }
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

    open fun toast(msg: String?) {
        ToastUtil.toast(msg)
    }

    protected open fun getTitleBarView(): TitleBarView? {
        return null
    }

    /**
     * 默认点击事件为finish()，可重写
     */
    protected open fun leftIconClickEvent() {
        finish()
    }

    open fun destroySubscription(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

}