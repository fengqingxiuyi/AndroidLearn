package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common.base.BaseActivity
import com.example.learn.aroutertest.IProviderTest
import com.example.learn.aroutertest.SingleInstance
import com.example.learn.ui.recyclerview.ChadRecyclerViewActivity
import com.example.learn.ui.recyclerview.SimpleRecyclerViewActivity
import com.example.learn.uiutils.UiUtilsActivity
import com.example.ui.toast.ToastUtil
import com.example.utils.activity.ActivitiesManager
import com.example.utils.app.AppUtil
import kotlin.math.abs

@Route(path = "/app/main")
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** 点两次退出应用 start  */
    private var lastClickTime: Long = 0

    private fun exitApp(): Boolean {
        val time = System.currentTimeMillis()
        if (abs(time - lastClickTime) < 2000) {
            return true
        }
        lastClickTime = time
        return false
    }

    override fun onBackPressed() {
        if (exitApp()) {
            ActivitiesManager.getInstance().finishAllActivity()
            finish()
        } else {
            ToastUtil.toast("再按一次退出程序")
        }
    }
    /** 点两次退出应用 end  */

    override fun onDestroy() {
        super.onDestroy()
        ToastUtil.onAppExit()
        Config.destroy()
        AppUtil.exitApp(this)
    }

    fun test(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun testLifecycle(view: View) {
        startActivity(Intent(this, LifecycleActivity::class.java))
    }

    fun testUiUtils(view: View) {
        startActivity(Intent(this, UiUtilsActivity::class.java))
    }

    fun testARouter(view: View) {
        println("xxxxx = " + SingleInstance.getInstance("xxx").testProvider)
//        val provider = ARouter.getInstance().navigation(SingleInstance::class.java)
//        provider.testSingleInstance()
        val provider1 = ARouter.getInstance().navigation(IProviderTest::class.java)
        provider1.testSingleInstance()
        println("yyyyyy = " + SingleInstance.getInstance("xxx").testProvider)
    }

    fun testEmpty(view: View) {
        startActivity(Intent(this, ChadRecyclerViewActivity::class.java))
    }
}