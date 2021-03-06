package com.example.learn.ui.constraint

import android.os.Bundle
import android.widget.TextView
import com.example.annotation.BindCompile
import com.example.annotation.Binding
import com.example.common.base.BaseActivity
import com.example.learn.R

/**
 * @author fqxyi
 * @date 2020/8/13
 */
class ConstraintActivity : BaseActivity() {

    //编译时注解-测试在另外一个类中的情况
    @BindCompile(R.id.myTextView) var myTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint)
        testAnnotation()
    }

    private fun testAnnotation() {
        //编译时注解
        Binding.bindCompile(this)
        myTextView?.text = "Binding Compile Success"
    }

}