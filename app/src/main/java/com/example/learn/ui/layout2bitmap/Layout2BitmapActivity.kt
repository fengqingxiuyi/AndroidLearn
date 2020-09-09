package com.example.learn.ui.layout2bitmap

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.common.base.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.activity_layout2bitmap.*

/**
 * Layout 转换为 Bitmap
 */
class Layout2BitmapActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout2bitmap)
        lbBtn.setOnClickListener { lbImg.setImageBitmap(getBitmap(lbLayout)) }
    }

    fun getBitmap(view: View): Bitmap? {
        val width = view.measuredWidth
        val height = view.measuredHeight
        if (width <= 0 || height <= 0) {
            Toast.makeText(this, "view's width or height are <= 0", Toast.LENGTH_SHORT).show()
            return null
        }
        val bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        if (bm == null) {
            Toast.makeText(this, "bitmap is null", Toast.LENGTH_SHORT).show()
            return null
        }
        val canvas = Canvas(bm)
        view.draw(canvas)
        return bm
    }
}