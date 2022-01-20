package com.example.utils.thread

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

abstract class Worker<T>(val scope: CoroutineScope) {

  private val taskChannel: Channel<T> = Channel(UNLIMITED)
  private var runJob: Job? = null
  fun run() {
    runJob = scope.launch {
      try {
        for (task in taskChannel) {
          try {
            doTask(task)
          } catch (e: Exception) {
            onDoTaskError(e)
          }
        }
      } catch (e: Exception) {
        Log.e("Worker", "Worker do task error! error is \n$e")
      }
    }
  }

  abstract suspend fun doTask(work: T)

  open fun onDoTaskError(e: Throwable) {}

  fun enqueueTask(work: T) {
    scope.launch {
      Log.d(
        "",
        "Worker: scope#isActive=${this.isActive}, channel#sendClosed=${taskChannel.isClosedForSend}|receiveClosed=${taskChannel.isClosedForReceive}"
      )
      if (!taskChannel.isClosedForSend)
        taskChannel.send(work)
    }
  }

  fun close() {
    taskChannel.close()
  }
}