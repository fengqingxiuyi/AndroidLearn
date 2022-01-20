package com.example.utils.other

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Utils {
  lateinit var context: Context
    private set

  fun init(application: Application) {
    this.context = application
  }

}