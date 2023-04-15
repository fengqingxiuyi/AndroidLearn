package com.example.learn.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.learn.uiutils.UIUtils;

/**
 * @author shenBF
 */
public class FqxyiRelativeLayout extends RelativeLayout {
    public FqxyiRelativeLayout(Context context) {
        super(context);
    }

    public FqxyiRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FqxyiRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FqxyiRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float scaleX = UIUtils.getInstance(getContext()).getHorValue();
//        float scaleY = UIUtils.getInstance(getContext()).getVerValue();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.width = (int) (layoutParams.width * scaleX);
            layoutParams.height = (int) (layoutParams.height * scaleX);
            layoutParams.leftMargin = (int) (layoutParams.leftMargin * scaleX);
            layoutParams.topMargin = (int) (layoutParams.topMargin * scaleX);
            layoutParams.rightMargin = (int) (layoutParams.rightMargin * scaleX);
            layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * scaleX);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setPadding(
                (int) (getPaddingLeft() * getScaleX()),
                (int) (getPaddingTop() * getScaleX()),
                (int) (getPaddingRight() * getScaleX()),
                (int) (getPaddingBottom() * getScaleX())
        );
    }
}
