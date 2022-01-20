package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.annotation.BindCompile
import com.example.aop.annotation.CrashSafe
import com.example.common.base.BaseActivity
import com.example.learn.annotation.BindRuntime
import com.example.learn.annotation.Binding
import com.example.learn.fps.Takt
import com.example.learn.iconchange.IconChangeConstant
import com.example.learn.iconchange.IconChangeManager
import com.example.learn.jetpack.room.RoomTest
import com.example.learn.jetpack.viewmodel.view.ViewModelActivity
import com.example.learn.jetpack.workmanager.WorkManagerTest
import com.example.learn.koin.MyViewModel
import com.example.learn.ui.adapter.ListCommonAdapterActivity
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
import com.example.learn.ui.layout2bitmap.Layout2BitmapActivity
import com.example.learn.ui.loop.LoopActivity
import com.example.learn.ui.network.hello.NetworkHelloActivity
import com.example.learn.ui.partition.PartitionActivity
import com.example.learn.ui.player.PlayerActivity
import com.example.learn.ui.recyclerview.RecyclerViewActivity
import com.example.learn.ui.refresh.RefreshTestActivity
import com.example.learn.ui.social.SocialActivity
import com.example.learn.ui.viewswitcher.ViewSwitcherActivity
import com.example.learn.ui.webview.WebViewSimpleActivity
import com.example.learn.ui.youtu.YoutuTestActivity
import com.example.ui.toast.ToastUtil
import com.example.log.LogUtil
import com.example.utils.device.StatusBarUtil
import com.example.webview_module.WebviewActivity
import com.example.webview_module.constants.WebviewConstant
import kotlinx.android.synthetic.main.activity_test.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * @author fqxyi
 * @date 2020/8/31
 * 测试页面
 */
class TestActivity : BaseActivity() {

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
        setContentView(R.layout.activity_test)
        testStatusBar()
        testViewAttribute()
        testAnnotation()
        testKoin()
        testRoom()
        testWorkManager()
    }

    private fun testStatusBar() {
        StatusBarUtil.setStatusBarTransparent(this)
        StatusBarUtil.createStatusView(this)
    }

    private fun testViewAttribute() {
        // 外部调用设置属性值
        viewAttribute.setViewImageRounded(false)
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

    private fun testRoom() {
        RoomTest.testRoom(this)
    }

    private fun testWorkManager() {
        WorkManagerTest.testWorkManager(this)
    }

    fun testConstraint(view: View) {
        startActivity(Intent(this, ConstraintActivity::class.java))
    }

    fun testViewSwitcher(view: View) {
        startActivity(Intent(this, ViewSwitcherActivity::class.java))
    }

    fun testImageScaleType(view: View) {
        startActivity(Intent(this, ImageScaleTypeActivity::class.java))
    }

    fun testDownload(view: View) {
        startActivity(Intent(this, DownloadActivity::class.java))
    }

    fun testLoadGif(view: View) {
        val intent = Intent(this, StarAnimActivity::class.java)
        intent.putExtra(StarIntentParamBean.TITLE, "点个赞")
        intent.putExtra(StarIntentParamBean.CONTENT, "完成任务了，继续加油！")
        startActivity(intent)
    }

    fun testAppbarLayout(view: View) {
        startActivity(Intent(this, AppbarLayoutActivity::class.java))
    }

    fun testAppbarLayout2(view: View) {
        startActivity(Intent(this, AppbarLayoutActivity2::class.java))
    }

    fun testWebViewSimple(view: View) {
        startActivity(Intent(this, WebViewSimpleActivity::class.java))
    }

    fun testWebView(view: View) {
        val intent = Intent(this, WebviewActivity::class.java)
        intent.putExtra(WebviewConstant.EXTRA_URL, "https://m.mamhao.com/")
        startActivity(intent)
    }

    /**
     * 测试在前台切换应用ICON，会杀掉应用，
     * 并且会出现点两次应用ICON才能启动应用的问题，所以应该在后台切换ICON
     */
    fun testIconChange(view: View) {
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

    fun testFps(view: View) {
        Takt.play(true)
    }

    fun testYoutu(view: View) {
        startActivity(Intent(this, YoutuTestActivity::class.java))
    }

    /**
     * external：Kotlin里标识一个方法是JNI方法的关键字
     */
    external fun stringFromJNI(): String?

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    fun testJNI(view: View) {
        ToastUtil.toast(stringFromJNI())
    }

    @CrashSafe
    fun testAOP(view: View) {
        val arr = arrayListOf<Int>()
        print(arr[1])
    }

    fun testLayout2Bitmap(view: View) {
        startActivity(Intent(this, Layout2BitmapActivity::class.java))
    }

    fun testList(view: View) {
        startActivity(Intent(this, ListCommonAdapterActivity::class.java))
    }

    fun testViewModel(view: View) {
        startActivity(Intent(this, ViewModelActivity::class.java))
    }

    fun testPlayer(view: View) {
        startActivity(Intent(this, PlayerActivity::class.java))
    }

    fun testRecyclerView(view: View) {
        startActivity(Intent(this, RecyclerViewActivity::class.java))
    }

}