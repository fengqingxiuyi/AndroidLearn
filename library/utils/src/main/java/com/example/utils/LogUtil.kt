package com.example.utils

import android.os.Looper
import android.text.TextUtils
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

/**
 * @author fqxyi
 * @date 2020/8/19
 * 所有log都通过这个类打印
 */
object LogUtil {

    private const val TAG = "LogUtil"
    private val LOG_DEBUG = BuildConfig.DEBUG
    private const val maxLength = 3500
    private const val SUFFIX = ".java"

    private var errorListener: ErrorListener? = null
    fun setErrorListener(errorListener: ErrorListener?) {
        LogUtil.errorListener = errorListener
    }

    private var logListener: LogListener? = null
    fun setLogListener(logListener: LogListener?) {
        LogUtil.logListener = logListener
    }

    private fun isEmpty(msg: String, lineBreak: Boolean): Boolean {
        try {
            if (TextUtils.isEmpty(msg)) {
                return true
            }
        } catch (e: Exception) { //在非Android环境中运行会报错
            if (lineBreak) {
                println(msg)
            } else {
                print(msg)
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun i(msg: String, lineBreak: Boolean = true) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return
            val header = getHeaderInfo()
            var index = 0
            val countOfSub = msg.length / maxLength
            if (countOfSub > 0) {
                for (i in 0 until countOfSub) {
                    val sub = msg.substring(index, index + maxLength)
                    Log.i(TAG, header + sub)
                    index += maxLength
                }
                Log.i(TAG, header + msg.substring(index, msg.length))
            } else {
                Log.i(TAG, header + msg)
            }
            // 回调
            logListener?.log(header + msg)
        }
    }

    @JvmStatic
    fun i(vararg messages: Any?) {
        if (LOG_DEBUG) {
            val stringBuilder = StringBuilder()
            for (msg in messages) {
                if (null == msg) {
                    continue
                }
                stringBuilder.append(msg.toString())
                stringBuilder.append(" ")
            }
            if (stringBuilder.isNotEmpty()) {
                i(stringBuilder.toString())
            }
        }
    }

    @JvmStatic
    fun d(msg: String, lineBreak: Boolean = true) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return
            val header = getHeaderInfo()
            var index = 0
            val countOfSub = msg.length / maxLength
            if (countOfSub > 0) {
                for (i in 0 until countOfSub) {
                    val sub = msg.substring(index, index + maxLength)
                    Log.d(TAG, header + sub)
                    index += maxLength
                }
                Log.d(TAG, header + msg.substring(index, msg.length))
            } else {
                Log.d(TAG, header + msg)
            }
            // 回调
            logListener?.log(header + msg)
        }
    }

    @JvmStatic
    fun d(vararg messages: Any?) {
        if (LOG_DEBUG) {
            val stringBuilder = StringBuilder()
            for (msg in messages) {
                if (null == msg) {
                    continue
                }
                stringBuilder.append(msg.toString())
                stringBuilder.append(" ")
            }
            if (stringBuilder.isNotEmpty()) {
                d(stringBuilder.toString())
            }
        }
    }

    @JvmStatic
    fun w(msg: String, lineBreak: Boolean = true) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return
            val header = getHeaderInfo()
            var index = 0
            val countOfSub = msg.length / maxLength
            if (countOfSub > 0) {
                for (i in 0 until countOfSub) {
                    val sub = msg.substring(index, index + maxLength)
                    Log.w(TAG, header + sub)
                    index += maxLength
                }
                Log.w(TAG, header + msg.substring(index, msg.length))
            } else {
                Log.w(TAG, header + msg)
            }
            // 回调
            logListener?.log(header + msg)
        }
    }

    @JvmStatic
    fun w(vararg messages: Any?) {
        if (LOG_DEBUG) {
            val stringBuilder = StringBuilder()
            for (msg in messages) {
                if (null == msg) {
                    continue
                }
                stringBuilder.append(msg.toString())
                stringBuilder.append(" ")
            }
            if (stringBuilder.isNotEmpty()) {
                w(stringBuilder.toString())
            }
        }
    }

    @JvmStatic
    fun e(msg: String, lineBreak: Boolean = true) {
        if (LOG_DEBUG) {
            if (isEmpty(msg, lineBreak)) return
            var index = 0
            val countOfSub = msg.length / maxLength
            if (countOfSub > 0) {
                for (i in 0 until countOfSub) {
                    val sub = msg.substring(index, index + maxLength)
                    Log.e(TAG, sub)
                    index += maxLength
                }
                Log.e(TAG, msg.substring(index, msg.length))
            } else {
                Log.e(TAG, msg)
            }
        }
        // 回调
        errorListener?.error(msg)
    }

    @JvmStatic
    fun e(vararg messages: String?) {
        val stringBuilder = StringBuilder()
        for (msg in messages) {
            stringBuilder.append(msg)
            stringBuilder.append("\n")
        }
        if (LOG_DEBUG) {
            e(stringBuilder.toString())
        } else {
            errorListener?.error(stringBuilder.toString())
        }
    }

    @JvmStatic
    fun e(tag: String, throwable: Throwable?) {
        if (LOG_DEBUG) {
            Log.e(tag, getFullHeaderInfo(), throwable)
        }
        // 回调
        errorListener?.error(throwable)
    }

    @JvmStatic
    fun e(throwable: Throwable?) {
        e(TAG, throwable)
    }

    @JvmStatic
    fun readThrowable(ex: Throwable): String {
        val writer: Writer = StringWriter()
        try {
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            var cause = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return writer.toString()
    }

    private fun getFullHeaderInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace ?: return ""
        val headInfo = StringBuilder()
        for (traceElement in stackTrace) {
            headInfo.append(traceElement.toString())
        }
        return headInfo.toString()
    }

    private fun getHeaderInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace ?: return ""
        var destStackTraceElement: StackTraceElement? = null
        var next = true
        for (traceElement in stackTrace) {
            if (traceElement.className == LogUtil::class.java.name) {
                next = false
            } else {
                if (next) {
                    continue
                } else {
                    destStackTraceElement = traceElement
                    break
                }
            }
        }
        if (null == destStackTraceElement) {
            return ""
        }
        val className = destStackTraceElement.className
        val methodName = destStackTraceElement.methodName
        var lineNumber = destStackTraceElement.lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }
        var threadInfo = "Thread: " + Thread.currentThread().name
        threadInfo += if (Looper.myLooper() == Looper.getMainLooper()) {
            "(UI线程), "
        } else {
            "(Work线程), "
        }
        val classAndMethodInfo = "[($className$SUFFIX:$lineNumber)#$methodName]\n"
        return threadInfo + classAndMethodInfo
    }

    interface ErrorListener {
        fun error(e: Throwable?)
        fun error(error: String?)
    }

    interface LogListener {
        fun log(e: Throwable?)
        fun log(error: String?)
    }
}