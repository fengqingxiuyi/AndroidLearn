package com.example.utils.other

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Utils {
  lateinit var context:Context
  private set

  fun init(context: Context){
    this.context = context
  }

}