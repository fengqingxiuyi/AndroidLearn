package com.example.learn.anim.star

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.learn.R
import com.example.utils.anim.AnimFinishCallback
import com.example.utils.anim.AnimStatusListener

/**
 * @author fqxyi
 * @date 2018/3/17
 * 点赞高光动画管理类
 */
class StarAnimManager(activity: Activity) : AnimStatusListener {

    private var frameLayout: FrameLayout
    private var starAnimView: StarAnimView

    init {
        val viewGroup = activity.window.decorView as ViewGroup
        frameLayout = FrameLayout(activity)
        // 定义布局管理器的参数
        frameLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.id = R.id.root_view
        viewGroup.addView(frameLayout)
        // 创建点赞高光View
        starAnimView = StarAnimView(activity)
        frameLayout.addView(starAnimView)
        frameLayout.visibility = View.GONE
        starAnimView.initAnim(activity, this)
    }
    
    fun setTitle(title: String?) {
        starAnimView.setTitle(title)
    }

    fun setContent(content: String?) {
        starAnimView.setContent(content)
    }

    fun startAnim() {
        starAnimView.startAnim()
    }

    fun onDestroy() {
        starAnimView.onDestroy()
    }

    override fun onStartListener() {
        frameLayout.visibility = View.VISIBLE
    }

    override fun onFinishListener() {
        frameLayout.visibility = View.GONE
        animFinishCallback?.onAnimFinish()
    }

    private var animFinishCallback: AnimFinishCallback? = null

    fun setAnimFinishCallback(animFinishCallback: AnimFinishCallback) {
        this.animFinishCallback = animFinishCallback
    }
}