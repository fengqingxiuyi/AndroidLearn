package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.annotation.BindCompile
import com.example.learn.annotation.BindRuntime
import com.example.learn.annotation.Binding
import com.example.learn.constraint.ConstraintActivity
import com.example.learn.jetpack.room.RoomTest
import com.example.learn.jetpack.workmanager.WorkManagerTest
import com.example.learn.koin.MyViewModel
import com.example.learn.ui.imagescaletype.ImageScaleTypeActivity
import com.example.learn.ui.viewswitcher.ViewSwitcherActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
//    val myViewModel: MyViewModel by viewModel()
    val myViewModel: MyViewModel by viewModel { parametersOf("koin from activity") }

    //运行时注解
    @BindRuntime(R.id.bindRuntimeText)
    var bindRuntimeText: TextView? = null

    //编译时注解
    @BindCompile(R.id.bindCompileText)
    var bindCompileText: TextView? = null
    @BindCompile(R.id.bindCompileText2)
    var bindCompileText2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testAnnotation()
        testKoin()
        RoomTest.testRoom(this)
        WorkManagerTest.testWorkManager(this)
    }

    private fun testAnnotation() {
        //运行时注解
        Binding.bindRuntime(this)
        bindRuntimeText?.text = "Binding Runtime Success"
        //编译时注解
        com.example.annotation.Binding.bindCompile(this)
        bindCompileText?.text = "Binding Compile Success"
        bindCompileText2?.text = "Binding Compile Success 2"
    }

    private fun testKoin() {
        Log.i("KOIN_TEST", myViewModel.sayHello())
    }

    fun openConstraint(view: View) {
        startActivity(Intent(this, ConstraintActivity::class.java))
    }

    fun openViewSwitcher(view: View) {
        startActivity(Intent(this, ViewSwitcherActivity::class.java))
    }

    fun openImageScaleType(view: View) {
        startActivity(Intent(this, ImageScaleTypeActivity::class.java))
    }
}