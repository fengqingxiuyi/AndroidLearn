package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.annotation.BindCompile
import com.example.common.base.BaseActivity
import com.example.learn.annotation.BindRuntime
import com.example.learn.annotation.Binding
import com.example.learn.iconchange.IconChangeConstant
import com.example.learn.iconchange.IconChangeManager
import com.example.learn.jetpack.room.RoomTest
import com.example.learn.jetpack.workmanager.WorkManagerTest
import com.example.learn.koin.MyViewModel
import com.example.learn.ui.anim.bean.StarIntentParamBean
import com.example.learn.ui.anim.star.StarAnimActivity
import com.example.learn.ui.appbarlayout.AppbarLayoutActivity
import com.example.learn.ui.appbarlayout.AppbarLayoutActivity2
import com.example.learn.ui.banner.BannerActivity
import com.example.learn.ui.card.CardActivity
import com.example.learn.ui.constraint.ConstraintActivity
import com.example.learn.ui.download.DownloadActivity
import com.example.learn.ui.image.ImageActivity
import com.example.learn.ui.imagescaletype.ImageScaleTypeActivity
import com.example.learn.ui.loop.LoopActivity
import com.example.learn.ui.network.hello.NetworkHelloActivity
import com.example.learn.ui.partition.PartitionActivity
import com.example.learn.ui.refresh.RefreshTestActivity
import com.example.learn.ui.social.SocialActivity
import com.example.learn.ui.viewswitcher.ViewSwitcherActivity
import com.example.learn.ui.webview.WebViewSimpleActivity
import com.example.ui.toast.ToastUtil
import com.example.utils.ActivitiesManager
import com.example.utils.LogUtil
import com.example.utils.device.StatusBarUtil
import com.example.webview_module.WebviewActivity
import com.example.webview_module.constants.WebviewConstant
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

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
        intent.putExtra(WebviewConstant.EXTRA_URL, "https://m.mamhao.com/")
        startActivity(intent)
    }

    /**
     * 测试在前台切换应用ICON，会杀掉应用，
     * 并且会出现点两次应用ICON才能启动应用的问题，所以应该在后台切换ICON
     */
    fun iconChange(view: View) {
        IconChangeManager.changeIcon(this, IconChangeConstant.CHANGE)
    }

    fun testPartition(view: View) {
        startActivity(Intent(this, PartitionActivity::class.java))
    }

    fun testNetwork(view: View) {
        startActivity(Intent(this, NetworkHelloActivity::class.java))
    }

    fun testSocial(view: View) {
        startActivity(Intent(this, SocialActivity::class.java))
    }

    fun testBanner(view: View) {
        startActivity(Intent(this, BannerActivity::class.java))
    }

    fun testRefresh(view: View) {
        startActivity(Intent(this, RefreshTestActivity::class.java))
    }

    fun testImage(view: View) {
        startActivity(Intent(this, ImageActivity::class.java))
    }

    fun testLoop(view: View) {
        startActivity(Intent(this, LoopActivity::class.java))
    }

    fun testCard(view: View) {
        startActivity(Intent(this, CardActivity::class.java))
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
    }
}