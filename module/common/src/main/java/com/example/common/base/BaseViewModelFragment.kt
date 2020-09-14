package com.example.common.base

import android.content.Context

/**
 * 不需要Presenter时继承他
 */
abstract class BaseViewModelFragment<T : BaseViewModel<*>> : BaseFragment() {

    @JvmField
    protected var mViewModel: T? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = getViewModel()
    }

    /**
     * 默认空实现
     */
    protected abstract fun getViewModel(): T?
}