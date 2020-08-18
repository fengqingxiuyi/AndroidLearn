package com.example.learn.ui.imagescaletype

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.learn.R
import com.example.learn.ui.imagescaletype.ImageScaleTypeAdapter.MainViewHolder
import kotlinx.android.synthetic.main.activity_image_scale_type_item.view.*

/**
 * @author fqxyi
 * @date 2018/1/27
 */
class ImageScaleTypeAdapter(private val context: Context) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.activity_image_scale_type_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // item背景
        if (position % 2 == 0) {
            holder.viewImage.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.viewImage.setBackgroundColor(Color.GRAY)
        }
        when (position) {
            0 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.CENTER
                holder.viewText.text = "CENTER"
            }
            1 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.CENTER_CROP
                holder.viewText.text = "CENTER_CROP"
            }
            2 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                holder.viewText.text = "CENTER_INSIDE"
            }
            3 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.FIT_START
                holder.viewText.text = "FIT_START"
            }
            4 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.FIT_CENTER
                holder.viewText.text = "FIT_CENTER"
            }
            5 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.FIT_END
                holder.viewText.text = "FIT_END"
            }
            6 -> {
                holder.viewImage.scaleType = ImageView.ScaleType.FIT_XY
                holder.viewText.text = "FIT_XY"
            }
            else -> {
                holder.viewImage.scaleType = ImageView.ScaleType.MATRIX
                holder.viewText.text = "MATRIX"
            }
        }
        // item点击事件
        holder.viewRoot.setOnClickListener {
            mOnItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return 8
    }

    class MainViewHolder(itemView: View) : ViewHolder(itemView) {
        val viewRoot: LinearLayout = itemView.viewRoot
        val viewImage: ImageView = itemView.viewImage
        val viewText: TextView = itemView.viewText
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener?) {
        this.mOnItemClickListener = mOnItemClickListener
    }

}