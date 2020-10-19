package com.example.network.observer

import io.reactivex.Observer

interface IBaseObserver<T> : Observer<T> {
    fun dispose()
}