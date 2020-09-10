package com.example.ui.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.ui.R;

public class RoundImageView extends AppCompatImageView {

    private float width, height;

    private Path path;

    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    public RoundImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        path = new Path();
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        radius = array.getDimensionPixelOffset(R.styleable.RoundImageView_radius, 0);
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_left_top_radius, 0);
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_right_top_radius, 0);
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_right_bottom_radius, 0);
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.RoundImageView_left_bottom_radius, 0);
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (radius != 0) {
            leftTopRadius = radius;
            rightTopRadius = radius;
            rightBottomRadius = radius;
            leftBottomRadius = radius;
        }
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
        int maxRight = Math.max(rightTopRadius, rightBottomRadius);
        int minWidth = maxLeft + maxRight;
        int maxTop = Math.max(leftTopRadius, rightTopRadius);
        int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
        int minHeight = maxTop + maxBottom;
        if (width >= minWidth && height > minHeight) {
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius, 0);
            path.quadTo(width, 0, width, rightTopRadius);

            path.lineTo(width, height - rightBottomRadius);
            path.quadTo(width, height, width - rightBottomRadius, height);

            path.lineTo(leftBottomRadius, height);
            path.quadTo(0, height, 0, height - leftBottomRadius);

            path.lineTo(0, leftTopRadius);
            path.quadTo(0, 0, leftTopRadius, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        leftTopRadius = radius;
        rightTopRadius = radius;
        rightBottomRadius = radius;
        leftBottomRadius = radius;
        invalidate();
    }

    public void setLeftTopRadius(int leftTopRadius) {
        this.leftTopRadius = leftTopRadius;
        invalidate();
    }

    public void setRightTopRadius(int rightTopRadius) {
        this.rightTopRadius = rightTopRadius;
        invalidate();
    }

    public void setRightBottomRadius(int rightBottomRadius) {
        this.rightBottomRadius = rightBottomRadius;
        invalidate();
    }

    public void setLeftBottomRadius(int leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
        invalidate();
    }
}
