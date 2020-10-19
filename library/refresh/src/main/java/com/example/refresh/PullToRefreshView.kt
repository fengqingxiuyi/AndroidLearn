package com.example.refresh

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.widget.*
import java.util.*

class PullToRefreshView : LinearLayout {
    /**
     * 控件
     */
    private var mInflater: LayoutInflater? = null

    //// Header
    private var mHeaderView: View? = null

    // Header-样式2
    private var mHeaderArrowpbContainer: LinearLayout? = null
    private var mHeaderArrowpb: RelativeLayout? = null
    private var mHeaderArrowpbPb: RoundProgressBar? = null
    private var mHeaderArrowpbArrow: ImageView? = null
    private var mHeaderArrowpbLoading: ProgressBar? = null
    private var mHeaderArrowpbTip: TextView? = null
    private var mHeaderArrowpbTime: TextView? = null

    // Header-样式3
    private var mHeaderArrowContainer: LinearLayout? = null
    private var mHeaderArrow: ImageView? = null
    private var mHeaderArrowLoading: ProgressBar? = null
    private var mHeaderArrowTip: TextView? = null
    private var mHeaderArrowTime: TextView? = null

    //// Footer
    private var mFooterView: View? = null

    // Footer-样式1
    private var mFooterImg: ImageView? = null
    private var mFooterTipTxt: TextView? = null
    private var mFooterRefreshPB: ProgressBar? = null

    //// mAdapterView可以包含的View类型
    private var mAdapterView: AdapterView<*>? = null
    private var mScrollView: ScrollView? = null
    private var mWebView: WebView? = null
    private var mOtherView: View? = null
    private var mViewGroup: ViewGroup? = null

    // 变为向下的箭头,改变箭头方向
    private var mFlipAnimation: RotateAnimation? = null

    // 变为逆向的箭头,旋转
    private var mReverseFlipAnimation: RotateAnimation? = null

    /**
     * 监听器
     */
    // 刷新加载监听器
    private var mOnHeaderRefreshListener: OnHeaderRefreshListener? = null
    private var mOnFooterRefreshListener: OnFooterRefreshListener? = null

    // 滑动距离监听器
    private var listener: DisYListener? = null

    /**
     * 设置头部下拉时的文字
     */
    // 提示文案
    var headerPullLabel = "下拉即可刷新"
    /**
     * 设置头部释放时的文字
     */
    var headerReleaseLabel = "松开即可刷新"
    /**
     * 设置头部刷新时的文字
     */
    var headerRefreshLabel = "正在刷新"
    /**
     * 设置底部上拉时的文字
     */
    var footerPullLabel = "上拉加载更多"
    /**
     * 设置底部释放后的文字
     */
    var footerReleaseLabel = "释放加载更多"
    /**
     * 设置底部加载时的文字
     */
    var footerRefreshLabel = "正在加载更多"

    /*
	通用
	 */
    // 记录拉动和刷新的状态
    private var mPullState = 0

    // 记录手指点击的位置
    private var mLastMotionY = 0
    private var mLastMotionX = 0

    // 记录当前头部或底部的拉动刷新状态
    private var mHeaderState = 0
    private var mFooterState = 0

    // 记录当前头部或底部View的高度
    private var mHeaderViewHeight = 0
    private var mFooterViewHeight = 0

    /*
	头部
	 */
    // 头部滑动偏移量，值得范围为0~newTopMargin
    private val headerOffset = 0

    // 如果头部布局是 Header-样式1 ，则需要此字段进行首次偏移量的判断
    private val isHeaderGirlOffsetFirst = true

    // mHeaderArrowpbPb显示的进度
    private var tempProgress = 0

    /**
     * 设置是否允许下拉刷新
     */
    var isEnableRefresh = true
    /**
     * 设置是否允许加载更多
     */
    var isEnableLoadMore = true

    /*
	1 头部布局为小女孩
	2 头部布局为进度条+箭头
	3 头部布局为箭头
	 */
    private var headerRefreshType = 0

    /**
     * 设置是否显示最近更新时间提示文案
     */
    var isShowTime = false

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        // 获取自定义的属性
        initAttrs(context, attrs)
        init()
    }

    //获取到attrs中的属性
    private fun initAttrs(
        context: Context,
        attrs: AttributeSet?
    ) {
        if (attrs != null) {
            val typeArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.PullToRefreshView, 0, 0
            )
            headerRefreshType = typeArray.getInt(
                R.styleable.PullToRefreshView_headerRefreshType,
                ARROW_TEXT
            )
            typeArray.recycle()
        }
    }

    private fun init() {
        // Load all of the animations we need in code rather than through XML
        orientation = VERTICAL
        mFlipAnimation = RotateAnimation(
            0f, (-180).toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        mFlipAnimation!!.interpolator = LinearInterpolator()
        mFlipAnimation!!.duration = 180
        mFlipAnimation!!.fillAfter = true
        mReverseFlipAnimation = RotateAnimation(
            (-180).toFloat(), 0f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        mReverseFlipAnimation!!.interpolator = LinearInterpolator()
        mReverseFlipAnimation!!.duration = 180
        mReverseFlipAnimation!!.fillAfter = true
        mInflater = LayoutInflater.from(context)
        // header view 在此添加,保证是第一个添加到LinearLayout的最上端
        addHeaderView()
    }

    private fun addHeaderView() {
        // header view
        mHeaderView = mInflater!!.inflate(R.layout.refresh_header, this, false)
        // Header-样式2
        mHeaderArrowpbContainer =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb_container) as LinearLayout
        mHeaderArrowpb =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb) as RelativeLayout
        mHeaderArrowpbPb =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb_pb) as RoundProgressBar
        mHeaderArrowpbLoading =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb_loading) as ProgressBar
        mHeaderArrowpbArrow =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb_arrow) as ImageView
        mHeaderArrowpbArrow!!.scaleType = ImageView.ScaleType.MATRIX
        mHeaderArrowpbTip =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb_tip) as TextView
        mHeaderArrowpbTime =
            mHeaderView!!.findViewById<View>(R.id.header_arrowpb_time) as TextView
        // Header-样式3
        mHeaderArrowContainer =
            mHeaderView!!.findViewById<View>(R.id.header_arrow_container) as LinearLayout
        mHeaderArrow =
            mHeaderView!!.findViewById<View>(R.id.header_arrow) as ImageView
        mHeaderArrow!!.scaleType = ImageView.ScaleType.MATRIX
        mHeaderArrowLoading =
            mHeaderView!!.findViewById<View>(R.id.header_arrow_loading) as ProgressBar
        mHeaderArrowTip =
            mHeaderView!!.findViewById<View>(R.id.header_arrow_tip) as TextView
        mHeaderArrowTime =
            mHeaderView!!.findViewById<View>(R.id.header_arrow_time) as TextView
        showCurrentHeader()

        // header layout
        measureView(mHeaderView)
        mHeaderViewHeight = mHeaderView!!.getMeasuredHeight()
        if (PB_ARROW_TEXT == headerRefreshType) {
            // 设置显示进度条的总进度
            mHeaderArrowpbPb!!.setMax(mHeaderViewHeight)
        }

        // 设置topMargin的值为负的header View高度,即将其隐藏在最上方
        val params =
            LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight)
        params.topMargin = -mHeaderViewHeight
        addView(mHeaderView, params)
    }

    private fun showCurrentHeader() {
        when (headerRefreshType) {
            PB_ARROW_TEXT -> {
                mHeaderArrowpbContainer!!.visibility = View.VISIBLE
                mHeaderArrowpb!!.visibility = View.VISIBLE
                mHeaderArrowContainer!!.visibility = View.GONE
            }
            ARROW_TEXT -> {
                mHeaderArrowpbContainer!!.visibility = View.GONE
                mHeaderArrowpb!!.visibility = View.GONE
                mHeaderArrowContainer!!.visibility = View.VISIBLE
            }
        }
    }

    private fun measureView(child: View?) {
        var p = child!!.layoutParams
        if (p == null) {
            p = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width)
        val lpHeight = p.height
        val childHeightSpec: Int
        childHeightSpec = if (lpHeight > 0) {
            MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY)
        } else {
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        }
        child.measure(childWidthSpec, childHeightSpec)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // footer view 在此添加保证添加到LinearLayout中的最后
        addFooterView()
        initContentAdapterView()
    }

    private fun addFooterView() {
        // footer view
        mFooterView = mInflater!!.inflate(R.layout.refresh_footer, this, false)
        mFooterImg =
            mFooterView!!.findViewById<View>(R.id.img_footer) as ImageView
        mFooterTipTxt =
            mFooterView!!.findViewById<View>(R.id.txt_footer_tip) as TextView
        mFooterRefreshPB =
            mFooterView!!.findViewById<View>(R.id.pb_footer_refresh) as ProgressBar
        // footer layout
        measureView(mFooterView)
        mFooterViewHeight = mFooterView!!.getMeasuredHeight()
        // 由于是线性布局可以直接添加,
        // 只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后, 并隐藏
        val params =
            LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight)
        addView(mFooterView, params)
    }

    /**
     * 初始化包含的view
     */
    private fun initContentAdapterView() {
        val count = childCount
        require(count >= 3) { "SHPullToRefreshView can only contain one view!" }
        var view: View
        for (i in 0 until count - 1) {
            view = getChildAt(i)
            if (view === mFooterView) {
                continue
            }
            if (view === mHeaderView) {
                continue
            }
            if (view is AdapterView<*>) {
                mAdapterView = view
                if (mAdapterView is AbsListView) {
                    val absListView = mAdapterView as AbsListView
                    absListView.setOnScrollListener(object : AbsListView.OnScrollListener {
                        override fun onScrollStateChanged(
                            view: AbsListView,
                            scrollState: Int
                        ) {
                            when (scrollState) {
                                AbsListView.OnScrollListener.SCROLL_STATE_IDLE ->                                    // 滑动到底部，自动触发上拉加载操作
                                    if (view.lastVisiblePosition == view.count - 1) {
                                        // 判断是否禁用上拉加载更多操作
                                        if (!isEnableLoadMore) {
                                            return
                                        }
                                        footerRefreshing()
                                    }
                            }
                        }

                        override fun onScroll(
                            view: AbsListView,
                            firstVisibleItem: Int,
                            visibleItemCount: Int,
                            totalItemCount: Int
                        ) {
                        }
                    })
                }
            } else if (view is ScrollView) {
                // finish later
                mScrollView = view
                mScrollView!!.viewTreeObserver
                    .addOnScrollChangedListener(object : OnScrollChangedListener {
                        override fun onScrollChanged() {
                            // 滑动到底部，自动触发上拉加载操作
                            if (mScrollView!!.scrollY == mScrollView!!.getChildAt(0)
                                    .height - mScrollView!!.height
                            ) {
                                // 判断是否禁用上拉加载更多操作
                                if (!isEnableLoadMore) {
                                    return
                                }
                                footerRefreshing()
                            }
                        }
                    })
            } else if (view is WebView) {
                mWebView = view
            } else if (view is ViewGroup) {
                mViewGroup = view
            } else {
                mOtherView = view
            }
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val y = e.rawY.toInt()
        val x = e.rawX.toInt()
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                // 首先拦截down事件,记录y坐标
                mLastMotionY = y
                mLastMotionX = x
            }
            MotionEvent.ACTION_MOVE -> {
                var xDistance = 0
                var yDistance = 0
                // deltaY > 0 是向下运动, < 0是向上运动
                val deltaY = y - mLastMotionY
                val deltaX = x - mLastMotionX
                xDistance += Math.abs(deltaX)
                yDistance += Math.abs(deltaY)
                if (xDistance > yDistance) {
                    return false
                } else if (isRefreshViewScroll(deltaY)) {
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            }
        }
        return false
    }

    /**
     * 是否应该到了父View,即PullToRefreshView滑动
     *
     * @param deltaY , deltaY > 0 是向下运动, < 0是向上运动
     */
    private fun isRefreshViewScroll(deltaY: Int): Boolean {
        if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
            return false
        }
        // 对于ListView和GridView
        if (mAdapterView != null) {
            // 子view(ListView or GridView)滑动到最顶端
            if (deltaY > 0) {
                // 判断是否禁用下拉刷新操作
                if (!isEnableRefresh) {
                    return false
                }
                val child = mAdapterView!!.getChildAt(0)
                    ?: // 如果mAdapterView中没有数据,不拦截
                    return false
                if (mAdapterView!!.firstVisiblePosition == 0
                    && child.top == 0
                ) {
                    mPullState = PULL_DOWN_STATE
                    return true
                }
                val top = child.top
                val padding = mAdapterView!!.paddingTop
                if (mAdapterView!!.firstVisiblePosition == 0
                    && Math.abs(top - padding) <= 8
                ) { // 这里之前用3可以判断,但现在不行,还没找到原因
                    mPullState = PULL_DOWN_STATE
                    return true
                }
            } else if (deltaY < 0) {
                // 判断是否禁用上拉加载更多操作
                if (!isEnableLoadMore) {
                    return false
                }
                val lastChild =
                    mAdapterView!!.getChildAt(mAdapterView!!.childCount - 1)
                        ?: // 如果mAdapterView中没有数据,不拦截
                        return false
                // 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
                // 等于父View的高度说明mAdapterView已经滑动到最后
                if (lastChild.bottom <= height
                    && mAdapterView!!.lastVisiblePosition == mAdapterView!!.count - 1
                ) {
                    mPullState = PULL_UP_STATE
                    return true
                }
            }
        } else if (mScrollView != null) {
            // 子scroll view滑动到最顶端
            val child = mScrollView!!.getChildAt(0)
            if (deltaY > 0 && mScrollView!!.scrollY == 0) {
                // 判断是否禁用下拉刷新操作
                if (!isEnableRefresh) {
                    return false
                }
                mPullState = PULL_DOWN_STATE
                return true
            } else if (deltaY < 0
                && child.measuredHeight <= height + mScrollView!!.scrollY
            ) {
                // 判断是否禁用上拉加载更多操作
                if (!isEnableLoadMore) {
                    return false
                }
                mPullState = PULL_UP_STATE
                return true
            }
        } else if (mWebView != null) {
            if (deltaY > 0 && mWebView!!.scrollY == 0) {
                // 判断是否禁用下拉刷新操作
                if (!isEnableRefresh) {
                    return false
                }
                mPullState = PULL_DOWN_STATE
                return true
            } else if (deltaY < 0
                && mWebView!!.contentHeight <= height
            ) {
                // 判断是否禁用上拉加载更多操作
                if (!isEnableLoadMore) {
                    return false
                }
                mPullState = PULL_UP_STATE
                return true
            }
        } else if (null != mOtherView) {
            if (deltaY > 0 && mOtherView!!.scrollY == 0) {
                // 判断是否禁用下拉刷新操作
                if (!isEnableRefresh) {
                    return false
                }
                mPullState = PULL_DOWN_STATE
                return true
            } else if (deltaY < 0) {
                // 判断是否禁用上拉加载更多操作
                if (!isEnableLoadMore) {
                    return false
                }
                mPullState = PULL_UP_STATE
                return true
            }
        } else if (null != mViewGroup) {
            if (deltaY > 0 && mViewGroup!!.scrollY == 0) {
                // 判断是否禁用下拉刷新操作
                if (!isEnableRefresh) {
                    return false
                }
                mPullState = PULL_DOWN_STATE
                return true
            } else if (deltaY < 0
                && mViewGroup!!.measuredHeight <= height + mViewGroup!!.scrollY
            ) {
                // 判断是否禁用上拉加载更多操作
                if (!isEnableLoadMore) {
                    return false
                }
                mPullState = PULL_UP_STATE
                return true
            }
        }
        return false
    }

    /*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return false;)
	 * 则由PullToRefreshView的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = y - mLastMotionY
                if (mPullState == PULL_DOWN_STATE) {
                    // PullToRefreshView执行下拉
                    Log.i(
                        TAG,
                        " pull down!parent view move!"
                    )
                    headerPrepareToRefresh(deltaY)
                    // setHeaderPadding(-mHeaderViewHeight);
                } else if (mPullState == PULL_UP_STATE) {
                    // PullToRefreshView执行上拉
                    Log.i(TAG, "pull up!parent view move!")
                    footerPrepareToRefresh(deltaY)
                }
                mLastMotionY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val topMargin = headerTopMargin
                if (listener != null) {
                    listener!!.getTopMargin(
                        Math.abs(topMargin),
                        mHeaderViewHeight + mFooterViewHeight, true
                    )
                }
                if (mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        // 开始刷新
                        headerRefreshing()
                    } else {
                        // 还没有执行刷新，重新隐藏
                        headerTopMargin = -mHeaderViewHeight
                    }
                } else if (mPullState == PULL_UP_STATE) {
                    if (Math.abs(topMargin) >= mHeaderViewHeight
                        + mFooterViewHeight
                    ) {
                        // 开始执行footer刷新
                        footerRefreshing()
                    } else {
                        // 还没有执行刷新，重新隐藏
                        headerTopMargin = -mHeaderViewHeight
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * header准备刷新,手指移动过程,还没有释放
     *
     * @param deltaY,手指滑动的距离
     */
    private fun headerPrepareToRefresh(deltaY: Int) {
        val newTopMargin = changingHeaderViewTopMargin(deltaY)
        changeTopMargin(newTopMargin, 0)
    }

    private fun changeTopMargin(newTopMargin: Int, offset: Int) {
        if (ARROW_TEXT == headerRefreshType) {
            if (newTopMargin < offset && Math.abs(newTopMargin) <= mHeaderViewHeight) {
                tempProgress = mHeaderViewHeight + newTopMargin
                rotate(mHeaderArrow, 1.0f * tempProgress / mHeaderViewHeight * 180)
            } else {
                if (tempProgress < mHeaderViewHeight) {
                    rotate(mHeaderArrow, 180f)
                }
            }
            // 当header view的topMargin>=offset时，说明已经完全显示出来了,修改header view的提示状态
            if (newTopMargin > offset && mHeaderState != RELEASE_TO_REFRESH) {
                mHeaderArrowTip!!.text = headerReleaseLabel
                if (isShowTime) {
                    mHeaderArrowTime!!.visibility = View.VISIBLE
                }
                mHeaderState = RELEASE_TO_REFRESH
            } else if (newTopMargin < offset && newTopMargin > -mHeaderViewHeight) { // 拖动时没有释放
                // mHeaderImageView.
                mHeaderArrowTip!!.text = headerPullLabel
                mHeaderState = PULL_TO_REFRESH
            }
        } else {
            if (PB_ARROW_TEXT == headerRefreshType) {
                if (newTopMargin < offset && Math.abs(newTopMargin) <= mHeaderViewHeight) {
                    tempProgress = mHeaderViewHeight + newTopMargin
                    mHeaderArrowpbPb!!.setProgress(tempProgress)
                    rotate(mHeaderArrowpbArrow, 1.0f * tempProgress / mHeaderViewHeight * 180)
                } else {
                    if (tempProgress < mHeaderViewHeight) {
                        mHeaderArrowpbPb!!.setProgress(mHeaderViewHeight)
                        rotate(mHeaderArrowpbArrow, 180f)
                    }
                }
            }
            // 当header view的topMargin>=offset时，说明已经完全显示出来了,修改header view的提示状态
            if (newTopMargin > offset && mHeaderState != RELEASE_TO_REFRESH) {
                mHeaderArrowpbTip!!.text = headerReleaseLabel
                if (isShowTime) {
                    mHeaderArrowpbTime!!.visibility = View.VISIBLE
                }
                mHeaderState = RELEASE_TO_REFRESH
            } else if (newTopMargin < offset && newTopMargin > -mHeaderViewHeight) { // 拖动时没有释放
                // mHeaderImageView.
                mHeaderArrowpbTip!!.text = headerPullLabel
                mHeaderState = PULL_TO_REFRESH
            }
        }
    }

    private fun rotate(imageView: ImageView?, degree: Float) {
        if (null == imageView) {
            return
        }
        val bitmapDrawable = imageView.drawable as BitmapDrawable ?: return
        val bitmap = bitmapDrawable.bitmap
        val matrix = Matrix()
        val width = bitmap.width
        val height = bitmap.height
        matrix.postRotate(degree, width / 2.toFloat(), height / 2.toFloat())
        if (PB_ARROW_TEXT == headerRefreshType) {
            val dispalyWidth = dp2px(10)
            val scale = 1.0f * dispalyWidth / width
            matrix.postScale(scale, scale)
        }
        if (ARROW_TEXT == headerRefreshType) {
            val dispalyWidth = dp2px(25)
            val scale = 1.0f * dispalyWidth / width
            matrix.postScale(scale, scale)
        }
        imageView.imageMatrix = matrix
    }

    private fun dp2px(dpVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal.toFloat(), resources
                .displayMetrics
        ).toInt()
    }

    /**
     * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
     * 高度是一样，都是通过修改header view的topmargin的值来达到
     *
     * @param deltaY ,手指滑动的距离
     */
    private fun footerPrepareToRefresh(deltaY: Int) {
        val newTopMargin = changingHeaderViewTopMargin(deltaY)
        if (listener != null) {
            listener!!.getTopMargin(
                Math.abs(newTopMargin),
                mHeaderViewHeight + mFooterViewHeight,
                false
            )
            listener!!.getDisY(Math.abs(newTopMargin) - mHeaderViewHeight)
        }

        // 如果header view topMargin的绝对值大于或等于header + footer的高度
        // 说明footer view完全显示出来了，修改footer view的提示状态
        if (Math.abs(newTopMargin) >= mHeaderViewHeight + mFooterViewHeight
            && mFooterState != RELEASE_TO_REFRESH
        ) {
            mFooterTipTxt!!.text = footerReleaseLabel
            mFooterImg!!.clearAnimation()
            mFooterImg!!.startAnimation(mFlipAnimation)
            mFooterState = RELEASE_TO_REFRESH
        } else if (Math.abs(newTopMargin) < mHeaderViewHeight + mFooterViewHeight) {
            mFooterImg!!.clearAnimation()
            mFooterImg!!.startAnimation(mFlipAnimation)
            mFooterTipTxt!!.text = footerPullLabel
            mFooterState = PULL_TO_REFRESH
        }
    }

    /**
     * 修改Header view top margin的值
     *
     * @param deltaY
     * @return
     */
    private fun changingHeaderViewTopMargin(deltaY: Int): Int {
        val params =
            mHeaderView!!.layoutParams as LayoutParams
        val newTopMargin =
            params.topMargin + deltaY * rate

        // 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了
        // 表示如果是在上拉后一段距离,然后直接下拉
        if (deltaY > 0 && mPullState == PULL_UP_STATE && params.topMargin < 0 && Math.abs(params.topMargin) <= mHeaderViewHeight
        ) {
            return params.topMargin
        }
        // 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
        if (deltaY < 0 && mPullState == PULL_DOWN_STATE && params.topMargin < 0 && Math.abs(params.topMargin) >= mHeaderViewHeight
        ) {
            return params.topMargin
        }
        params.topMargin = newTopMargin.toInt()
        mHeaderView!!.layoutParams = params
        invalidate()
        return params.topMargin
    }

    fun headerRefreshing() {
        mHeaderState = REFRESHING
        headerTopMargin = 0
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener!!.onHeaderRefresh(this)
        }
        if (PB_ARROW_TEXT == headerRefreshType) {
            mHeaderArrowpbPb!!.visibility = View.GONE
            mHeaderArrowpbArrow!!.visibility = View.GONE
            mHeaderArrowpbArrow!!.clearAnimation()
            mHeaderArrowpbArrow!!.setImageDrawable(null)
            mHeaderArrowpbLoading!!.visibility = View.VISIBLE
            mHeaderArrowpbTip!!.text = headerRefreshLabel
        } else if (ARROW_TEXT == headerRefreshType) {
            mHeaderArrow!!.visibility = View.GONE
            mHeaderArrow!!.clearAnimation()
            mHeaderArrow!!.setImageDrawable(null)
            mHeaderArrowLoading!!.visibility = View.VISIBLE
            mHeaderArrowTip!!.text = headerRefreshLabel
        }
    }

    private fun footerRefreshing() {
        mFooterState = REFRESHING
        val top = mHeaderViewHeight + mFooterViewHeight
        headerTopMargin = -top
        mFooterImg!!.visibility = View.GONE
        mFooterImg!!.clearAnimation()
        mFooterImg!!.setImageDrawable(null)
        mFooterRefreshPB!!.visibility = View.VISIBLE
        mFooterTipTxt!!.text = footerRefreshLabel
        if (mOnFooterRefreshListener != null) {
            mOnFooterRefreshListener!!.onFooterRefresh(this)
        }
    }

    /**
     * header view完成更新后恢复初始状态
     */
    fun onHeaderRefreshComplete() {
        headerTopMargin = -mHeaderViewHeight
        mHeaderState = PULL_TO_REFRESH
        if (isShowTime) {
            setLastUpdated("最近更新:" + Date().toLocaleString())
        }
        if (PB_ARROW_TEXT == headerRefreshType) {
            mHeaderArrowpbPb!!.visibility = View.VISIBLE
            mHeaderArrowpbArrow!!.visibility = View.VISIBLE
            mHeaderArrowpbArrow!!.setImageResource(R.drawable.header_arrow_purple)
            mHeaderArrowpbLoading!!.visibility = View.GONE
            mHeaderArrowpbTip!!.text = headerPullLabel
        } else if (ARROW_TEXT == headerRefreshType) {
            mHeaderArrow!!.visibility = View.VISIBLE
            mHeaderArrow!!.setImageResource(R.drawable.header_arrow_grey)
            mHeaderArrowLoading!!.visibility = View.GONE
            mHeaderArrowTip!!.text = headerPullLabel
        }
    }

    fun onHeaderRefreshComplete(lastUpdated: CharSequence?) {
        if (isShowTime) {
            setLastUpdated(lastUpdated)
        }
        onHeaderRefreshComplete()
    }

    /**
     * footer view完成更新后恢复初始状态
     */
    fun onFooterRefreshComplete() {
        headerTopMargin = -mHeaderViewHeight
        mFooterImg!!.visibility = View.VISIBLE
        mFooterImg!!.setImageResource(R.drawable.footer_arrow_grey)
        mFooterTipTxt!!.text = footerPullLabel
        mFooterRefreshPB!!.visibility = View.GONE
        if (isShowTime) {
            setLastUpdated("")
        }
        mFooterState = PULL_TO_REFRESH
    }

    /**
     * footer view完成更新后恢复初始状态
     */
    fun onFooterRefreshComplete(size: Int) {
        if (size > 0) {
            mFooterView!!.visibility = View.VISIBLE
        } else {
            mFooterView!!.visibility = View.GONE
        }
        headerTopMargin = -mHeaderViewHeight
        mFooterImg!!.visibility = View.VISIBLE
        mFooterImg!!.setImageResource(R.drawable.footer_arrow_grey)
        mFooterTipTxt!!.text = footerPullLabel
        mFooterRefreshPB!!.visibility = View.GONE
        if (isShowTime) {
            setLastUpdated("")
        }
        mFooterState = PULL_TO_REFRESH
    }

    /**
     * 动态设置显示的刷新的时间
     * @param lastUpdated
     */
    fun setLastUpdated(lastUpdated: CharSequence?) {
        if (PB_ARROW_TEXT == headerRefreshType) {
            if (lastUpdated != null) {
                mHeaderArrowpbTime!!.visibility = View.VISIBLE
                mHeaderArrowpbTime!!.text = lastUpdated
            } else {
                mHeaderArrowpbTime!!.visibility = View.GONE
            }
        } else if (ARROW_TEXT == headerRefreshType) {
            if (lastUpdated != null) {
                mHeaderArrowTime!!.visibility = View.VISIBLE
                mHeaderArrowTime!!.text = lastUpdated
            } else {
                mHeaderArrowTime!!.visibility = View.GONE
            }
        }
    }

    /**
     * 滑动距离回调监听
     */
    interface DisYListener {
        fun getDisY(disY: Int)
        fun getTopMargin(
            topMargin: Int,
            totalHeight: Int,
            isHandRelease: Boolean
        )
    }

    fun setDisYListener(listener: DisYListener?) {
        this.listener = listener
    }

    /**
     * 设置头部下拉刷新的回调
     *
     * @param headerRefreshListener
     */
    fun setOnHeaderRefreshListener(
        headerRefreshListener: OnHeaderRefreshListener?
    ) {
        mOnHeaderRefreshListener = headerRefreshListener
    }

    /**
     * 设置底部上拉刷新的回调
     *
     * @param footerRefreshListener
     */
    fun setOnFooterRefreshListener(
        footerRefreshListener: OnFooterRefreshListener?
    ) {
        mOnFooterRefreshListener = footerRefreshListener
    }

    fun showFootView(show: Boolean) {
        if (show) {
            mFooterView!!.visibility = View.VISIBLE
        } else {
            mFooterView!!.visibility = View.GONE
        }
    }

    /**
     * 设置header view的topMargin的值
     */
    private var headerTopMargin: Int
        private get() {
            val params =
                mHeaderView!!.layoutParams as LayoutParams
            return params.topMargin
        }
        private set(topMargin) {
            val params =
                mHeaderView!!.layoutParams as LayoutParams
            params.topMargin = topMargin
            mHeaderView!!.layoutParams = params
            invalidate()
        }

    interface OnFooterRefreshListener {
        fun onFooterRefresh(view: PullToRefreshView?)
    }

    interface OnHeaderRefreshListener {
        fun onHeaderRefresh(view: PullToRefreshView?)
    }

    /**
     * 样式2-设置提示颜色
     * @param color
     */
    fun setHeaderArrowpbTipTextColor(color: Int): PullToRefreshView {
        mHeaderArrowpbTip!!.setTextColor(resources.getColor(color))
        return this
    }

    /**
     * 样式2-设置时间颜色
     * @param color
     */
    fun setHeaderArrowpbTimeTextColor(color: Int): PullToRefreshView {
        mHeaderArrowpbTime!!.setTextColor(resources.getColor(color))
        return this
    }

    /**
     * 样式3-设置提示颜色
     * @param color
     */
    fun setHeaderArrowTipTextColor(color: Int): PullToRefreshView {
        mHeaderArrowTip!!.setTextColor(resources.getColor(color))
        return this
    }

    /**
     * 样式3-设置时间颜色
     * @param color
     */
    fun setHeaderArrowTimeTextColor(color: Int): PullToRefreshView {
        mHeaderArrowTime!!.setTextColor(resources.getColor(color))
        return this
    }

    companion object {
        private const val TAG = "PullToRefreshView"
        private const val PB_ARROW_TEXT = 2
        private const val ARROW_TEXT = 3

        /**
         * 数据
         */
        // 拉动状态
        private const val PULL_UP_STATE = 0
        private const val PULL_DOWN_STATE = 1

        // 刷新状态
        private const val PULL_TO_REFRESH = 2
        private const val RELEASE_TO_REFRESH = 3
        private const val REFRESHING = 4

        // rate值越大，滑动相同的距离，头部下滑的距离更大
        private const val rate = 0.5f
    }
}