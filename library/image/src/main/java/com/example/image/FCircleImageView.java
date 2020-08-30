package com.example.image;

import android.content.Context;
import android.util.AttributeSet;

import com.example.image.fresco.FrescoCircleImageView;
import com.facebook.drawee.generic.GenericDraweeHierarchy;

/**
 * @author fqxyi
 * @date 2018/2/22
 * 圆形图 业务实现类
 */
public class FCircleImageView extends FrescoCircleImageView {

    public FCircleImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FCircleImageView(Context context) {
        super(context);
    }

    public FCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FCircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setBorderColor(int color) {
        super.setBorderColor(color);
    }

    @Override
    public void setBorderWidth(int width) {
        super.setBorderWidth(width);
    }
}
