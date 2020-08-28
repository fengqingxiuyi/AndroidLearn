package com.example.banner.listener;

import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * 翻页指示器适配器
 */
public class CustomPageChangeListener implements ViewPager.OnPageChangeListener {

    // Image类型指示点view和id集合
    private List<ImageView> pointImgViews;
    private int[] pointImgIds;
    // 自定义类型的布局数据
    private View pointView;
    private int pointSize;
    private PointChangeListener pointChangeListener;

    // 接口回调
    private ViewPager.OnPageChangeListener listener;
    public void setPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.listener = listener;
    }

    // 构造方法
    public CustomPageChangeListener(View pointView, int pointSize, PointChangeListener pointChangeListener) {
        this.pointView = pointView;
        this.pointSize = pointSize;
        this.pointChangeListener = pointChangeListener;
    }

    public CustomPageChangeListener(List<ImageView> pointImgViews, int[] pointImgIds) {
        this.pointImgViews = pointImgViews;
        this.pointImgIds = pointImgIds;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null) listener.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (listener != null) listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int index) {
        if (listener != null) listener.onPageSelected(index);
        if (null != pointChangeListener) {
            pointChangeListener.getInfo(pointView, index, pointSize);
        }
        // 指示点切换
        if (null != pointImgViews && pointImgViews.size() > 0) {
            for (int i = 0; i < pointImgViews.size(); i++) {
                if (index == i) {
                    if (null != pointImgViews.get(index)) {
                        pointImgViews.get(index).setImageResource(pointImgIds[1]);
                    }
                } else {
                    if (null != pointImgViews.get(i)) {
                        pointImgViews.get(i).setImageResource(pointImgIds[0]);
                    }
                }
            }
        }
    }

    public void setPointImgData(List<ImageView> pointImgViews, int[] pointImgIds) {
        this.pointImgViews = pointImgViews;
        this.pointImgIds = pointImgIds;
    }

    public void setPointChangeListener(View view, int size, PointChangeListener pointChangeListener) {
        this.pointView = view;
        this.pointSize = size;
        this.pointChangeListener = pointChangeListener;
    }
}
