package com.example.learn.ui.anim.star

import android.app.Activity
import android.os.Bundle
import com.example.learn.ui.anim.bean.StarIntentParamBean
import com.example.utils.anim.AnimFinishCallback

/**
 * @author fqxyi
 * @date 2018/3/6
 * 点赞高光动画
 * 需要设置android:launchMode="singleInstance"，防止快速点击的时候被多次触发
 */
class StarAnimActivity : Activity() {
    
    private val starAnimManager: StarAnimManager by lazy {
        StarAnimManager(this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra(StarIntentParamBean.TITLE)
        val content = intent.getStringExtra(StarIntentParamBean.CONTENT)
        starAnimManager.setTitle(title)
        starAnimManager.setContent(content)
        starAnimManager.startAnim()
        starAnimManager.setAnimFinishCallback(object : AnimFinishCallback {
            override fun onAnimFinish() {
                finish()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        starAnimManager.onDestroy()
    }
}