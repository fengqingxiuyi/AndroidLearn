package com.example.learn.ui.partition.partition.line;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.ui.partition.utils.Util;

/**
 * 分割线区块
 */
public class LineViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    public LineViewHolder(@NonNull View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        //分割线默认间距为10dp
        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(context, 10)));
        //分割线默认颜色为红色
        itemView.setBackgroundColor(Color.parseColor("#f6f6f6"));
    }

    /**
     * 自定义分割线样式
     */
    public void initData(int height) {
        initData(height, Color.parseColor("#f6f6f6"));
    }

    /**
     * 自定义分割线样式
     */
    public void initData(int height, int color) {
        itemView.getLayoutParams().height = Util.dip2px(context, height);
        itemView.setBackgroundColor(color);
    }

}
