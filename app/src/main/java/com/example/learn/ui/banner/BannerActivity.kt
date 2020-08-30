package com.example.learn.ui.banner

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.banner.TurnBanner
import com.example.banner.holder.DefaultImageHolderView
import com.example.banner.holder.Holder
import com.example.common.base.BaseActivity
import com.example.learn.R
import com.example.ui.toast.ToastUtil
import com.example.utils.device.DensityUtil

/**
 * @author fqxyi
 * @date 2020/8/28
 */
class BannerActivity : BaseActivity() {

    private val list = arrayListOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher_round
    )

    private val pointArr = intArrayOf(R.drawable.banner_dot_unselect, R.drawable.banner_dot_select)

    private var pointSpace: Int = 0

    private lateinit var turnBanner: TurnBanner<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        //
        pointSpace = DensityUtil.dp2px(this, 8f)
        //
        turnBanner = findViewById(R.id.turnBanner)
        turnBanner.setPages(getImageViewHolder())
            .setPointViewVisible(true)
            .setPageIndicator(pointArr, pointSpace, pointSpace)
            .startAutoTurn(3000)
            .setData(list)
    }

    private fun getImageViewHolder() : Holder<Int> {
        val imageHolderView = DefaultImageHolderView<Int>()
        imageHolderView.setScaleType(ImageView.ScaleType.FIT_CENTER)
        imageHolderView.setImageShowListener(object : DefaultImageHolderView.ImageShowListener<Int> {
            override fun showImage(imageView: ImageView, data: Int) {
                imageView.setImageResource(data)
            }
        })
        imageHolderView.setClickListener(object : DefaultImageHolderView.ImageClickListener<Int> {
            override fun click(view: View, position: Int, data: Int) {
                ToastUtil.toast("点击了第${position}张图片")
            }
        })
        return imageHolderView
    }

    override fun onDestroy() {
        super.onDestroy()
        turnBanner.destroy()
    }

}