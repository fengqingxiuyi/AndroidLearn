package com.example.learn.ui.imagescaletype

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.learn.R
import kotlinx.android.synthetic.main.activity_image_scale_type.*

/**
 * @author fqxyi
 * @date 2020/8/18
 */
class ImageScaleTypeActivity : AppCompatActivity() {

    private val dialogBuilder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(this@ImageScaleTypeActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testImageScaleType()
    }

    private fun testImageScaleType() {
        setContentView(R.layout.activity_image_scale_type)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val imageScaleTypeAdapter = ImageScaleTypeAdapter(this)
        imageScaleTypeAdapter.setOnItemClickListener(object : ImageScaleTypeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        dialogBuilder.setTitle("CENTER")
                        dialogBuilder.setMessage("按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示")
                    }
                    1 -> {
                        dialogBuilder.setTitle("CENTER_CROP")
                        dialogBuilder.setMessage("按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽) ")
                    }
                    2 -> {
                        dialogBuilder.setTitle("CENTER_INSIDE")
                        dialogBuilder.setMessage("将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽 ")
                    }
                    3 -> {
                        dialogBuilder.setTitle("FIT_START")
                        dialogBuilder.setMessage("把图片按比例扩大/缩小到View的宽度，顶部显示")
                    }
                    4 -> {
                        dialogBuilder.setTitle("FIT_CENTER")
                        dialogBuilder.setMessage("把图片按比例扩大/缩小到View的宽度，居中显示")
                    }
                    5 -> {
                        dialogBuilder.setTitle("FIT_END")
                        dialogBuilder.setMessage("把图片按比例扩大/缩小到View的宽度，底部显示")
                    }
                    6 -> {
                        dialogBuilder.setTitle("FIT_XY")
                        dialogBuilder.setMessage("不按比例缩放图片，目标是把图片塞满整个View")
                    }
                    else -> {
                        dialogBuilder.setTitle("MATRIX")
                        dialogBuilder.setMessage("显示矩阵所在区域的图片，不缩放图片")
                    }
                }
                dialogBuilder.create().show()
            }
        })
        recyclerView.adapter = imageScaleTypeAdapter
    }
}