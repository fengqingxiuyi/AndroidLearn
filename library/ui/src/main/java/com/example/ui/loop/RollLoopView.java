package com.example.ui.loop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * 自动向上翻滚的View
 */
public class RollLoopView extends FrameLayout {

    private ViewFlipper mViewFlipper;

    private Animation animIn;
    private Animation animOut;

    private static final int MILLISECONDS = 3000;

    public RollLoopView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RollLoopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RollLoopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        animIn = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0f);
        animIn.setDuration(500);
        animOut = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        animOut.setDuration(500);

        mViewFlipper = new ViewFlipper(context);
        mViewFlipper.setFlipInterval(MILLISECONDS);
        addView(mViewFlipper);
    }

    public void start(IItemView iItemView) {
        start(null, null, 0, iItemView);
    }

    public void start(int milliseconds, IItemView iItemView) {
        start(null, null, milliseconds, iItemView);
    }

    public void start(Animation animIn, Animation animOut, IItemView iItemView) {
        start(animIn, animOut, 0, iItemView);
    }

    public void start(Animation animIn, Animation animOut, int milliseconds, IItemView iItemView) {
        if (iItemView == null) {
            return;
        }
        List dataList = iItemView.getDataList();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        if (animIn == null) {
            animIn = this.animIn;
        }
        if (animOut == null) {
            animOut = this.animOut;
        }
        if (milliseconds < 1) {
            milliseconds = MILLISECONDS;
        }
        mViewFlipper.removeAllViews();
        for (int index = 0; index < dataList.size(); index++) {
            View view = iItemView.getItemView();
            iItemView.perform(view, index);
            mViewFlipper.addView(view);
        }
        mViewFlipper.setFlipInterval(milliseconds);
        if (dataList.size() == 1) {
            mViewFlipper.stopFlipping();
        } else {
            //使用代码添加动画
            mViewFlipper.setInAnimation(animIn);
            mViewFlipper.setOutAnimation(animOut);
            mViewFlipper.startFlipping();
        }
    }

}
