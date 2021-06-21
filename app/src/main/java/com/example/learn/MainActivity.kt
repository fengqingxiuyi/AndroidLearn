package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.common.base.BaseActivity
import com.example.learn.uiutils.UiUtilsActivity
import com.example.ui.toast.ToastUtil
import com.example.utils.activity.ActivitiesManager
import com.example.utils.app.AppUtil
import kotlin.math.abs

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

    fun testUiUtils(view: View) {
        startActivity(Intent(this, UiUtilsActivity::class.java))
    }
}