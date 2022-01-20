package com.example.utils.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (t: T) -> Unit) {
  liveData.observe(this, { it?.let { t -> observer(t) } })
}

fun <T> Fragment.observe(liveData: LiveData<T>, observer: (t: T) -> Unit) {
  this.viewLifecycleOwner.observe(liveData, observer)
}

fun LifecycleOwner.onStop(callback: () -> Unit) {
  lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
      super.onStop(owner)
      callback.invoke()
    }
  })
}

fun LifecycleOwner.onDestroy(callback: () -> Unit) {
  lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
      super.onDestroy(owner)
      callback.invoke()
    }
  })
}

fun LifecycleOwner.onResume(callback: () -> Unit) {
  lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
      super.onResume(owner)
      callback.invoke()
    }
  })
}

fun LifecycleOwner.onPause(callback: () -> Unit) {
  lifecycle.addObserver(object : DefaultLifecycleObserver {
    override fun onPause(owner: LifecycleOwner) {
      super.onPause(owner)
      callback.invoke()
    }
  })
}

fun <T> LifecycleOwner.observe(
  flow: Flow<T>,
  flowContext: CoroutineContext = Dispatchers.IO,
  observer: suspend (t: T) -> Unit
) = lifecycleScope.launch {
  flow.flowOn(flowContext).collectLatest {
    observer.invoke(it)
  }
}

fun <T> Fragment.observe(
  flow: Flow<T>,
  flowContext: CoroutineContext = Dispatchers.IO,
  observer: suspend (t: T) -> Unit
) = viewLifecycleOwner.observe(flow, flowContext, observer)
