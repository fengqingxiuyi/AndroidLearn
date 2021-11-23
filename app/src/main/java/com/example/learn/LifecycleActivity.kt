package com.example.learn

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.common.base.BaseActivity

class LifecycleActivity : BaseActivity() {

    private val TAG = "LifecycleActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle)
        println("$TAG => onCreate")
    }

    override fun onStart() {
        super.onStart()
        println("$TAG => onStart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        println("$TAG => onRestoreInstanceState")
    }

    override fun onRestart() {
        super.onRestart()
        println("$TAG => onRestart")
    }

    override fun onResume() {
        super.onResume()
        println("$TAG => onResume")
    }

    override fun onPause() {
        super.onPause()
        println("$TAG => onPause")
    }

    override fun onStop() {
        super.onStop()
        println("$TAG => onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("$TAG => onSaveInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("$TAG => onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        println("$TAG => onConfigurationChanged")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("$TAG => onNewIntent")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        println("$TAG => onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        println("$TAG => onDetachedFromWindow")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        println("$TAG => onWindowFocusChanged")
    }

    fun dialogOpen(view: View) {
        AlertDialog.Builder(this)
            .setTitle("dialog open")
            .setPositiveButton("确定") { dialog, which ->
                Toast.makeText(this, "dialog toast open", Toast.LENGTH_SHORT).show()
            }
            .create().show()
    }

    fun toastOpen(view: View) {
        Toast.makeText(this, "toast open", Toast.LENGTH_SHORT).show()
    }

    fun jumpMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

}