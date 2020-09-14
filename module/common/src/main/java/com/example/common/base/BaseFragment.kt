package com.example.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.network.RequestManager

/**
 * 不需要Presenter时继承他
 */
abstract class BaseFragment : Fragment() {

    @JvmField
    protected var mActivity: BaseActivity? = null
    @JvmField
    protected var appContext: Context? = null

    //loading
    var loadingView: View? = null
        private set

    private var rootView: View? = null
    var isFirstVisible = true
        private set
    private var isFirstInVisible = true

    //判断第一次有效载入fragment
    private var isActivityCreated = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context.applicationContext
        if (context is BaseActivity) {
            mActivity = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPresenter()
        initData(savedInstanceState)
        isActivityCreated = true
        judgeIsFirstVisible()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = initView(inflater, container)
        return rootView
    }

    fun addLoadingView(view: View?, params: FrameLayout.LayoutParams?) {
        if (rootView == null) return
        loadingView = view
        (rootView as ViewGroup).addView(view, params)
    }

    fun removeLoadingView() {
        val parent = loadingView?.parent ?: return
        (parent as ViewGroup).removeView(loadingView)
        loadingView = null
    }

    override fun onResume() {
        super.onResume()
        if (isFirstVisible) {
            isFirstVisible = false
        } else {
            onReUserVisible()
        }
    }

    override fun onDestroy() {
        RequestManager.get().destroy(this)
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onReUserVisible()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        judgeIsFirstVisible()
        if (userVisibleHint) {
            if (isFirstVisible) {
                isFirstVisible = false
            } else {
                onHiddenChanged(false)
            }
        } else {
            if (isFirstInVisible) {
                isFirstInVisible = false
            } else {
                onHiddenChanged(true)
            }
        }
    }

    /**
     * 判断是否第一次可见
     */
    private fun judgeIsFirstVisible() {
        if (isActivityCreated && userVisibleHint) {
            isActivityCreated = false
            onFirstUserVisible()
        }
    }

    /**
     * 第一次Fragment可见（进行初始化工作）
     */
    protected fun onFirstUserVisible() {}

    /**
     * 除第一次见到Fragment外，之后每次看见Fragment都会执行
     */
    protected fun onReUserVisible() {}

    /**
     * 初始化Presenter
     */
    protected fun initPresenter() {}

    /**
     * 初始化Presenter的attach方法
     */
    protected fun initPresenterAttach() {}

    /**
     * 初始化控件
     */
    protected abstract fun initView(inflater: LayoutInflater, container: ViewGroup?): View?

    /**
     * 初始化数据
     */
    protected abstract fun initData(savedInstanceState: Bundle?)
}