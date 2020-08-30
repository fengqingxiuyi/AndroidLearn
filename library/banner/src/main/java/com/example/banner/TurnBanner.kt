package com.example.banner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.banner.adapter.BannerPageAdapter
import com.example.banner.holder.Holder
import com.example.banner.listener.BannerItemClickListener
import com.example.banner.listener.CustomPageChangeListener
import com.example.banner.listener.PointChangeListener
import com.example.banner.view.BannerViewPager
import com.example.banner.view.ViewPagerScroller
import kotlinx.android.synthetic.main.banner_layout.view.*
import java.lang.ref.WeakReference
import java.util.*

/**
 * 页面翻转控件
 */
class TurnBanner<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    //layout
    private var bannerViewPager: BannerViewPager<T>

    // attr
    var isTurning = false //是否正在自动翻页
        private set
    private var autoTurnTime = 0L //自动翻页时间间隔
    var isCanTurn = false //能否手动触发翻页
    private var canLoop = true //是否支持无限循环

    // component
    private var scroller: ViewPagerScroller
    private var pageAdapter: BannerPageAdapter<T>? = null
    private var customPageChangeListener: CustomPageChangeListener? = null
    private var onPageChangeListener: OnPageChangeListener? = null
    private var switchTask: SwitchTask<T>

    // data
    var data: List<T>? = null
        private set
    private val pointViews = ArrayList<ImageView>()

    enum class PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    init {
        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TurnBanner)
            canLoop = typedArray.getBoolean(R.styleable.TurnBanner_canLoop, true)
            autoTurnTime =
                typedArray.getInteger(R.styleable.TurnBanner_autoTurningTime, 3000).toLong()
            typedArray.recycle()
        }
        LayoutInflater.from(context).inflate(R.layout.banner_layout, this, true)
        //
        bannerViewPager = findViewById(R.id.bannerViewPager)
        // 初始化ViewPager的滑动速度
        scroller = ViewPagerScroller(getContext())
        try {
            val mScroller = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            mScroller[bannerViewPager] = scroller
        } catch (e: Exception) {
            e.printStackTrace()
        }
        switchTask = SwitchTask(this)
    }

    /**
     * 设置ViewPager的滚动速度
     */
    fun setScrollDuration(scrollDuration: Int): TurnBanner<T> {
        scroller.scrollDuration = scrollDuration
        return this
    }

    /**
     * 初始化页面 但 不初始化数据
     */
    fun setPages(holderCreator: Holder<T>): TurnBanner<T> {
        setPages(holderCreator, null)
        return this
    }

    /**
     * 初始化页面 并且 初始化数据
     */
    fun setPages(holderCreator: Holder<T>, data: List<T>?): TurnBanner<T> {
        this.data = data
        pageAdapter = BannerPageAdapter<T>(holderCreator, data)
        bannerViewPager.setAdapter(pageAdapter!!, canLoop)
        return this
    }

    /**
     * 仅初始化数据
     */
    fun setData(data: List<T>?): TurnBanner<T> {
        this.data = data
        pageAdapter?.setData(data)
        return this
    }

    fun getDataSize(): Int {
        return data?.size ?: 0
    }

    /***
     * 开始翻页
     * @param time 翻页时间
     */
    fun startAutoTurn(time: Long): TurnBanner<T> {
        var autoTurnTime = time
        if (autoTurnTime < 1000) {
            autoTurnTime = 1000
        }
        //如果是正在翻页的话先停掉
        if (isTurning) {
            stopAutoTurn()
        }
        setCanScroll(true)
        //设置可以翻页并开启翻页
        isCanTurn = true
        //正在自动翻页
        isTurning = true
        //自动翻页时间间隔
        this.autoTurnTime = autoTurnTime
        //开始翻页
        postDelayed(switchTask, autoTurnTime)
        return this
    }

    /**
     * 开启翻页 如果是正在翻页的话先停止翻页，再启动翻页
     */
    fun startAutoTurn(): TurnBanner<T> {
        startAutoTurn(autoTurnTime)
        return this
    }

    /**
     * 暂停翻页 手动触摸会重新启动自动翻页
     */
    fun pauseAutoTurn(): TurnBanner<T> {
        isTurning = false
        removeCallbacks(switchTask)
        return this
    }

    /**
     * 停止翻页 禁止手动触摸
     */
    fun stopAutoTurn(): TurnBanner<T> {
        isCanTurn = false
        setCanScroll(false)
        pauseAutoTurn()
        return this
    }

    /**
     * 设置是否可以循环播放
     */
    fun setCanLoop(canLoop: Boolean) {
        this.canLoop = canLoop
        bannerViewPager.setCanLoop(canLoop)
    }

    fun isCanLoop(): Boolean {
        return bannerViewPager.isCanLoop()
    }

    /**
     * 设置是否可以滚动
     */
    fun setCanScroll(isCanScroll: Boolean) {
        bannerViewPager.isCanScroll = isCanScroll
    }

    fun isCanScroll(): Boolean {
        return bannerViewPager.isCanScroll
    }

    /**
     * 触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (isCanTurn) startAutoTurn()
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 暂停翻页
            if (isCanTurn) pauseAutoTurn()
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 设置当前的页面index
     */
    fun setCurrentItem(index: Int) {
        bannerViewPager.currentItem = index
    }

    /**
     * 获取当前的页面index
     */
    fun getCurrentItem(): Int {
        return bannerViewPager.getRealItem()
    }

    /**
     * 自定义指示器样式：. . . . .
     *
     * pointImgIds大小只能为2
     */
    fun setPageIndicator(pointImgIds: IntArray?, right: Int, bottom: Int): TurnBanner<T> {
        if (null == pointImgIds || pointImgIds.size != 2) {
            throw IllegalArgumentException("pointImgIds大小只能为2")
        }
        if (null == data) return this
        // clear view
        pointerContainer.removeAllViews()
        // clear data
        pointViews.clear()
        // get view
        for (count in data!!.indices) {
            val pointView = ImageView(context)
            pointView.setPadding(0, 0, right, bottom)
            if (pointViews.isEmpty()) {
                pointView.setImageResource(pointImgIds[1])
            } else {
                pointView.setImageResource(pointImgIds[0])
            }
            pointViews.add(pointView)
            pointerContainer.addView(pointView)
        }
        if (null == customPageChangeListener) {
            customPageChangeListener = CustomPageChangeListener(pointViews, pointImgIds)
        } else {
            customPageChangeListener!!.setPointImgData(pointViews, pointImgIds)
        }
        bannerViewPager.setPageChangeListener(customPageChangeListener)
        customPageChangeListener!!.setPageChangeListener(onPageChangeListener)
        return this
    }

    /**
     * 完全自定义的指示点布局
     * @param layoutId 外部传入的指示点布局
     */
    fun setPageIndicator(
        @LayoutRes layoutId: Int,
        pointChangeListener: PointChangeListener?
    ): TurnBanner<T> {
        if (null == data) return this
        // clear view
        pointerContainer.removeAllViews()
        // clear data
        pointViews.clear()
        // add view
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        pointerContainer.addView(view)
        // other
        if (null == customPageChangeListener) {
            customPageChangeListener =
                CustomPageChangeListener(view, data!!.size, pointChangeListener)
        } else {
            customPageChangeListener!!.setPointChangeListener(
                view,
                data!!.size,
                pointChangeListener
            )
        }
        bannerViewPager.setPageChangeListener(customPageChangeListener)
        customPageChangeListener!!.setPageChangeListener(onPageChangeListener)
        return this
    }

    /**
     * 设置底部指示器是否可见
     */
    fun setPointViewVisible(visible: Boolean): TurnBanner<T> {
        pointerContainer.visibility = if (visible) VISIBLE else GONE
        return this
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：
     * 居左 （RelativeLayout.ALIGN_PARENT_LEFT），
     * 居中 （RelativeLayout.CENTER_HORIZONTAL），
     * 居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     */
    fun setPageIndicatorAlign(align: PageIndicatorAlign): TurnBanner<T> {
        val layoutParams = pointerContainer.layoutParams as RelativeLayout.LayoutParams
        layoutParams.addRule(
            RelativeLayout.ALIGN_PARENT_LEFT,
            if (align == PageIndicatorAlign.ALIGN_PARENT_LEFT) RelativeLayout.TRUE else 0
        )
        layoutParams.addRule(
            RelativeLayout.ALIGN_PARENT_RIGHT,
            if (align == PageIndicatorAlign.ALIGN_PARENT_RIGHT) RelativeLayout.TRUE else 0
        )
        layoutParams.addRule(
            RelativeLayout.CENTER_HORIZONTAL,
            if (align == PageIndicatorAlign.CENTER_HORIZONTAL) RelativeLayout.TRUE else 0
        )
        pointerContainer.layoutParams = layoutParams
        return this
    }

    /**
     * 自定义翻页动画效果
     */
    fun setPageTransformer(transformer: ViewPager.PageTransformer?): TurnBanner<T> {
        bannerViewPager.setPageTransformer(true, transformer)
        return this
    }

    /**
     * 设置翻页监听器
     */
    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?): TurnBanner<T> {
        this.onPageChangeListener = onPageChangeListener
        // 如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (customPageChangeListener != null) {
            customPageChangeListener!!.setPageChangeListener(onPageChangeListener)
        } else {
            bannerViewPager.setPageChangeListener(onPageChangeListener)
        }
        return this
    }

    /**
     * 监听item点击
     */
    fun setOnItemClickListener(bannerItemClickListener: BannerItemClickListener?): TurnBanner<T> {
        bannerViewPager.setBannerItemClickListener(bannerItemClickListener)
        return this
    }

    fun destroy() {
        bannerViewPager.destroy()
    }

    internal class SwitchTask<T>(turnBanner: TurnBanner<T>) : Runnable {

        private val reference: WeakReference<TurnBanner<T>> = WeakReference(turnBanner)

        override fun run() {
            val turnBanner = reference.get()
            if (turnBanner != null) {
                if (turnBanner.bannerViewPager != null && turnBanner.isTurning) {
                    var page = turnBanner.bannerViewPager.currentItem + 1
                    if (page >= turnBanner.pageAdapter!!.count) page = 0
                    turnBanner.bannerViewPager.currentItem = page
                    turnBanner.postDelayed(turnBanner.switchTask, turnBanner.autoTurnTime)
                }
            }
        }

    }
}