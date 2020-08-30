package com.example.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.example.image.fresco.FrescoImageView;
import com.example.image.listener.IImageLoadListener;
import com.example.image.listener.IImageView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

/**
 * @author fqxyi
 * @date 2018/2/22
 * 业务实现类
 */
public class FImageView extends FrescoImageView implements IImageView {

    public FImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FImageView(Context context) {
        super(context);
    }

    public FImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageCircle() {
        super.setImageCircle();
    }

    @Override
    public void setImageRound(float radius) {
        super.setImageRound(radius);
    }

    @Override
    public void setEnableWrapContent(boolean enableWrapContent) {
        super.setEnableWrapContent(enableWrapContent);
    }

    @Override
    public void setPlaceholderImage(int resource) {
        super.setPlaceholderImage(resource);
    }

    @Override
    public void setPlaceholderImage(int resource, ScalingUtils.ScaleType scaleType) {
        super.setPlaceholderImage(resource, scaleType);
    }

    @Override
    public void setPlaceholderImage(Drawable drawable) {
        super.setPlaceholderImage(drawable);
    }

    @Override
    public void setPlaceholderImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        super.setPlaceholderImage(drawable, scaleType);
    }

    @Override
    public void setImageUrl(int resId) {
        super.setImageUrl(resId);
    }

    @Override
    public void setImageUrl(int resId, IImageLoadListener loadListener) {
        super.setImageUrl(resId, loadListener);
    }

    @Override
    public void setImageUrl(int resId, int width, int height) {
        super.setImageUrl(resId, width, height);
    }

    @Override
    public void setImageUrl(String url) {
        super.setImageUrl(url);
    }

    @Override
    public void setImageUrl(String url, IImageLoadListener loadListener) {
        super.setImageUrl(url, loadListener);
    }

    @Override
    public void setImageUrl(String url, int width, int height) {
        super.setImageUrl(url, width, height);
    }

    @Override
    public String getImageUrl() {
        return super.getImageUrl();
    }

    @Override
    public int getImageRes() {
        return super.getImageRes();
    }
}
