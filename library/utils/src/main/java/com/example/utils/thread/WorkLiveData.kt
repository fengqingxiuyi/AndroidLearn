package com.example.utils.thread

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class WorkLiveData<T>(private val extraWork: (T?.() -> Unit)) {

  private val liveData = MutableLiveData<T>()

  val value get() = liveData.value

  fun postValue(value: T?, doWork: Boolean = true) {
    value?.run {

      if (doWork) extraWork.invoke(this)

      liveData.postValue(this)

    }
  }

  fun setValue(value: T, doWork: Boolean = true) {
    value?.run {

      if (doWork) extraWork.invoke(this)

      liveData.value = this

    }
  }

  fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    liveData.observe(owner, observer)
  }

  fun observeForever(observer: Observer<in T>) {
    liveData.observeForever(observer)
  }

  fun removeObserver(observer: Observer<in T>) {
    liveData.removeObserver(observer)
  }

  fun removeObservers(owner: LifecycleOwner) {
    liveData.removeObservers(owner)
  }

}