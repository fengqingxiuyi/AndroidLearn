package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.annotation.BindCompile
import com.example.common.BaseActivity
import com.example.learn.anim.bean.StarIntentParamBean
import com.example.learn.anim.star.StarAnimActivity
import com.example.learn.annotation.BindRuntime
import com.example.learn.annotation.Binding
import com.example.learn.constraint.ConstraintActivity
import com.example.learn.download.DownloadActivity
import com.example.learn.jetpack.room.RoomTest
import com.example.learn.jetpack.workmanager.WorkManagerTest
import com.example.learn.koin.MyViewModel
import com.example.learn.ui.appbarlayout.AppbarLayoutActivity
import com.example.learn.ui.appbarlayout.AppbarLayoutActivity2
import com.example.learn.ui.imagescaletype.ImageScaleTypeActivity
import com.example.learn.ui.viewswitcher.ViewSwitcherActivity
import com.example.learn.webview.WebViewSimpleActivity
import com.example.utils.LogUtil
import com.example.utils.StatusBarUtil
import com.example.webview.constants.WebviewConstant
import com.example.webview.view.WebviewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity() {

    // Lazy Inject ViewModel
//    val myViewModel: MyViewModel by viewModel()
    val myViewModel: MyViewModel by viewModel { parametersOf("koin from activity") }

    //运行时注解
    @BindRuntime(R.id.bindRuntimeText)
    var bindRuntimeText: TextView? = null

    //编译时注解
    @BindCompile(R.id.bindCompileText)
    var bindCompileText: TextView? = null

    //编译时注解-测试多个id的情况
    @BindCompile(R.id.bindCompileText2)
    var bindCompileText2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testStatusBar()
        testAnnotation()
        testKoin()
        RoomTest.testRoom(this)
        WorkManagerTest.testWorkManager(this)
    }

    private fun testStatusBar() {
        StatusBarUtil.setStatusBarTransparent(this)
        StatusBarUtil.createStatusView(this)
    }

    private fun testAnnotation() {
        //运行时注解
        Binding.bindRuntime(this)
        bindRuntimeText?.text = "Binding Runtime Success"
        //编译时注解
        com.example.annotation.Binding.bindCompile(this)
        bindCompileText?.text = "Binding Compile Success"
        bindCompileText2?.text = "Binding Compile Success 2"
    }

    private fun testKoin() {
        LogUtil.i("KOIN_TEST", myViewModel.sayHello())
    }

    fun openConstraint(view: View) {
        startActivity(Intent(this, ConstraintActivity::class.java))
    }

    fun openViewSwitcher(view: View) {
        startActivity(Intent(this, ViewSwitcherActivity::class.java))
    }

    fun openImageScaleType(view: View) {
        startActivity(Intent(this, ImageScaleTypeActivity::class.java))
    }

    fun openDownload(view: View) {
        startActivity(Intent(this, DownloadActivity::class.java))
    }

    fun loadGif(view: View) {
        val intent = Intent(this, StarAnimActivity::class.java)
        intent.putExtra(StarIntentParamBean.TITLE, "点个赞")
        intent.putExtra(StarIntentParamBean.CONTENT, "完成任务了，继续加油！")
        startActivity(intent)
    }

    fun openAppbarLayout(view: View) {
        startActivity(Intent(this, AppbarLayoutActivity::class.java))
    }

    fun openAppbarLayout2(view: View) {
        startActivity(Intent(this, AppbarLayoutActivity2::class.java))
    }

    fun openWebViewSimple(view: View) {
        startActivity(Intent(this, WebViewSimpleActivity::class.java))
    }

    fun openWebView(view: View) {
        val intent = Intent(this, WebviewActivity::class.java)
        intent.putExtra(WebviewConstant.EXTRA_URL, "http://www.baidu.com/")
        startActivity(intent)
    }
}