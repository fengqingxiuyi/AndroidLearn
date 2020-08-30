package com.example.image.listener;

import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.ScalingUtils;

/**
 * @author fqxyi
 * @date 2018/2/22
 */
public interface IImageView {

    void setImageCircle();

    void setImageRound(float radius);

    /**
     * 设置图片自适应宽高
     * 适用场景：宽和高 都可以是 WRAP_CONTENT，但是不能为 0
     *
     * @param enableWrapContent 是否自适应
     */
    void setEnableWrapContent(boolean enableWrapContent);

    void setPlaceholderImage(int resource);

    void setPlaceholderImage(int resource, ScalingUtils.ScaleType scaleType);

    void setPlaceholderImage(Drawable drawable);

    void setPlaceholderImage(Drawable drawable, ScalingUtils.ScaleType scaleType);

    void setImageUrl(int resId);

    void setImageUrl(int resId, IImageLoadListener loadListener);

    void setImageUrl(int resId, int width, int height);

    void setImageUrl(String url);

    void setImageUrl(String url, IImageLoadListener loadListener);

    void setImageUrl(String url, int width, int height);

    String getImageUrl();

    int getImageRes();

}
