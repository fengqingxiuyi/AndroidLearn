package com.example.learn.ui.banner

import android.os.Bundle
import android.widget.ImageView
import com.example.banner.holder.DefaultImageHolderView
import com.example.banner.holder.Holder
import com.example.common.base.BaseActivity
import com.example.learn.R
import com.example.ui.toast.ToastUtil
import com.example.utils.device.DensityUtil
import kotlinx.android.synthetic.main.activity_banner.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        //
        pointSpace = DensityUtil.dp2px(this, 8f)
        //
        turnBanner.setPages(getImageViewHolder())
            .setPointViewVisible(true)
            .setPageIndicator(pointArr, pointSpace, pointSpace)
            .startAutoTurn(3000)
            .setData(list)
    }

    private fun getImageViewHolder() : Holder<Int> {
        val imageHolderView = DefaultImageHolderView<Int>()
        imageHolderView.setScaleType(ImageView.ScaleType.FIT_CENTER)
        imageHolderView.setImageShowListener { imageView, data ->
            imageView.setImageResource(data)
        }
        imageHolderView.setClickListener { view, position, data ->
            ToastUtil.toast("点击了第${position}张图片")
        }
        return imageHolderView
    }

    override fun onDestroy() {
        super.onDestroy()
        turnBanner.destroy()
    }

}