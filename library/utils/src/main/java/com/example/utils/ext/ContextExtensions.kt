package com.example.utils.ext

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.widget.Toast
import com.example.utils.other.runOnUiThread
import java.io.BufferedReader
import java.io.InputStreamReader

inline fun <reified T> Context.launch() {
  this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Context.launch(body: Intent.() -> Unit) {
  val intent = Intent(this, T::class.java)
  intent.body()
  startActivity(intent)
}

fun Context.toastReal(value: CharSequence) {
  val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
  toast.setText(value)
  toast.show()
}

// android 7.x 系统（8.0上已修复）有BadTokenException
inline fun Context.toast(value: () -> CharSequence) {
  val str = value()
  if (Looper.myLooper() == null || Looper.myLooper() != Looper.getMainLooper()) {
    runOnUiThread {
      toastReal(str)
    }
  } else {
    toastReal(str)
  }
}

inline fun Context.toastRes(value: () -> Int) {
  toast { resources.getString(value.invoke()) }
}

inline fun Context.toastCenter(value: () -> CharSequence) {
  val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT).apply {
    setText(value.invoke())
  }
  toast.show()
}

inline fun Context.toastLong(value: () -> CharSequence) {
  val toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
  toast.setText(value.invoke())
  toast.show()
}


fun Context.readFromAssets(name: String): String {
  var bufferedReader: BufferedReader? = null
  val str = StringBuilder()
  try {
    val inputStream = assets.open(name)
    bufferedReader = BufferedReader(InputStreamReader(inputStream))
    var line = ""
    while (true) {
      line = bufferedReader.readLine()
      if (line.isEmpty())
        break
      str.append(line)
    }
  } catch (e: Exception) {
    e.printStackTrace()
  } finally {
    bufferedReader?.close()
    return str.toString()
  }
}