package com.example.common

import android.content.Context
import android.os.Bundle
import com.example.utils.permission.CheckPermissionActivity

/**
 * @author fqxyi
 * @date 2020/8/19
 * 业务基类
 */
open class BaseActivity : CheckPermissionActivity() {

    lateinit var activity: BaseActivity
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        context = applicationContext
    }

}