package com.example.common.base

import android.os.Bundle

/**
 * @author fqxyi
 * @date 2020/8/19
 * 业务基类
 */
abstract class BaseViewModelActivity<T : BaseViewModel<*>> : BaseActivity() {

    @JvmField
    protected var mViewModel: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    /**
     * 默认空实现
     */
    protected abstract fun getViewModel(): T?

}