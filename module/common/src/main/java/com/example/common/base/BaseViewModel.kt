package com.example.common.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel<T : BaseRepository<*>>(application: Application) :
    AndroidViewModel(application) {

    @JvmField
    protected var context: Context

    @JvmField
    protected var mRepository: T

    init {
        context = application
        mRepository = getRepository()
    }

    protected abstract fun getRepository(): T
}