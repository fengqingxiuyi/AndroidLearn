package com.example.learn.anim.star

import android.app.Activity
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.learn.R
import com.example.learn.anim.star.item.*
import com.example.utils.anim.AnimGather
import com.example.utils.anim.AnimGather.HandlerCall
import com.example.utils.anim.AnimStatusListener
import kotlinx.android.synthetic.main.view_star_anim.view.*

/**
 * @author fqxyi
 * @date 2018/3/6
 * 点赞高光动画实现
 */
class StarAnimView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var animStatusListener: AnimStatusListener? = null

    lateinit var animGather: AnimGather

    private val animRoot: StarAnimRoot by lazy {
        StarAnimRoot(viewRoot)
    }
    private val animTitle: StarAnimTitle by lazy {
        StarAnimTitle(viewTitle)
    }
    private val animContent: StarAnimContent by lazy {
        StarAnimContent(viewContent)
    }
    private val animCircle: StarAnimCircle by lazy {
        StarAnimCircle(viewCircle)
    }
    private val animThumb: StarAnimThumb by lazy {
        StarAnimThumb(viewThumb)
    }
    private val animStarDefault: StarAnimStarDefault by lazy {
        StarAnimStarDefault(viewStarDefault)
    }
    private val animStarOne: StarAnimStarOne by lazy {
        StarAnimStarOne(viewStarOne)
    }
    private val animStarTwo: StarAnimStarTwo by lazy {
        StarAnimStarTwo(viewStarTwo)
    }
    private val animStarThree: StarAnimStarThree by lazy {
        StarAnimStarThree(viewStarThree)
    }

    init {
        inflate(context, R.layout.view_star_anim, this);
    }

    fun setTitle(title: String?) {
        viewTitle.text = title
    }

    fun setContent(content: String?) {
        viewContent.text = content
    }

    fun initAnim(activity: Activity, animUpdateListener: AnimStatusListener?) {
        animStatusListener = animUpdateListener
        init()
        animGather = AnimGather(activity)
        animGather.addAnim(animRoot.createAnim(animUpdateListener), 360)
        animGather.addAnim(animTitle.createAnim(), 280)
        animGather.addAnim(animContent.createAnim(), 280)
        animGather.addAnim(animCircle.createAnim(), 360)
        animGather.addAnim(animThumb.createAnim(), 160)
        animGather.addAnim(animStarDefault.createAnim(), 720)
        animGather.addAnim(animStarOne.createAnim(), 720)
        animGather.addAnim(animStarTwo.createAnim(), 720)
        animGather.addAnim(animStarThree.createAnim(), 720)
    }

    fun startAnim() {
        init()
        if (animGather.isRunning()) {
            animGather.cancel()
        }
        animStatusListener?.onStartListener()
        animGather.startAnim()
        animGather.postRunOnUI(object : HandlerCall {
            override fun call(message: Message?) {
                val animationDrawable = viewLight.drawable as AnimationDrawable ?: return
                viewLight.visibility = View.VISIBLE
                if (animationDrawable.isRunning) {
                    animationDrawable.stop()
                }
                animationDrawable.start()
                animGather.postRunOnUI(object : HandlerCall {
                    override fun call(message: Message?) {
                        val animationDrawable2 = viewLight.drawable as AnimationDrawable ?: return
                        if (animationDrawable2.isRunning) {
                            animationDrawable2.stop()
                            viewLight.visibility = View.INVISIBLE
                        }
                    }
                }, Message(), 330)
            }
        }, Message(), 720)
    }

    private fun init() {
        viewLight.setImageResource(R.drawable.star_anim_light)
        animRoot.onRest()
        animCircle.onRest()
        animThumb.onRest()
        animStarDefault.onRest()
        animStarOne.onRest()
        animStarTwo.onRest()
        animStarThree.onRest()
    }

    fun onDestroy() {
        // 回收动画集合
        animGather.onDestroy()
    }
}