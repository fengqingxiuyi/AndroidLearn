package com.example.utils.other

import android.os.Handler
import android.os.Looper

/**
 * Provides handler and mainThreadScheduler.
 */
private object ContextHandler {
  val handler = Handler(Looper.getMainLooper())
  val mainThread = Looper.getMainLooper().thread
}

fun runOnUiThread(action: () -> Unit) {
  ContextHandler.handler.post {
    action()
  }
}

inline fun <T> tryOrElse(defaultValue: T, block: () -> T): T = tryOrNull(block)
  ?: defaultValue

inline fun <T> tryOrNull(block: () -> T): T? = try {
  block()
} catch (e: Exception) {
  null
}

inline fun <reified T> Any?.cast() = this as? T