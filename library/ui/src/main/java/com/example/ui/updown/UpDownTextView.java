package com.example.ui.updown;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UpDownTextView extends LinearLayout implements UpDownTimeTask, View.OnClickListener {

    private Context mContext;

    private TextView textViews[] = new TextView[3];

    private LinearLayout rootView;

    private String data;
    /***
     * 动画时间
     */
    private int mAnimTime = 500;

    /**
     * 停留时间
     */
    private int mStillTime = 500;
    /**
     * 轮播的数据
     */
    List<String> dataList;
    /***
     * 当前轮播的索引
     */
    private int currentIndex = 0;

    /***
     * 动画模式
     */
    private int animMode = ANIM_MODE_UP;// 默认向上 0--向上，1--向下

    public final static int ANIM_MODE_UP = 0;
    public final static int ANIM_MODE_DOWN = 1;
    private Handler timeHandler;
    private TimeRunnable runnable;

    private TranslateAnimation animationDown, animationUp;
    private UpDownListener upDownListener;

    @Override
    public void next(int currentIndex) {
        this.currentIndex = currentIndex;
        if (null == runnable) {
            runnable = new TimeRunnable(UpDownTextView.this, getContext(), dataList, animMode);
        }
        timeHandler.postDelayed(runnable, mStillTime + mAnimTime);
    }

    public UpDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        rootView = new LinearLayout(mContext);
        rootView.setOrientation(LinearLayout.VERTICAL);
        this.addView(rootView);
        if (null == timeHandler) {
            timeHandler = new Handler();
        }
        textViews[0] = addText();
        textViews[1] = addText();
        textViews[2] = addText();
        textViews[0].setOnClickListener(this);
        textViews[1].setOnClickListener(this);
        textViews[2].setOnClickListener(this);
    }

    /***
     * 当界面销毁时
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();// 防止内存泄漏的操作
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setViewsHeight();
    }

    /***
     * 重新设置VIEW的高度
     */
    private void setViewsHeight() {
        for (TextView tv : textViews) {
            LayoutParams lp = (LayoutParams) tv.getLayoutParams();
            lp.height = getHeight();
            lp.width = getWidth();
            tv.setLayoutParams(lp);
        }

        LayoutParams lp2 = (LayoutParams) rootView.getLayoutParams();
        lp2.height = getHeight() * (rootView.getChildCount());
        lp2.setMargins(0, -getHeight(), 0, 0);// 使向上偏移一定的高度，用padding,scrollTo都分有问题
        rootView.setLayoutParams(lp2);
    }

    private TextView addText() {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        rootView.addView(tv);
        return tv;
    }

    /***
     * 设置初始的字
     */
    public void setText(String text) {
        data = text;
        textViews[1].setText(text);
    }

    /***
     * 开始自动滚动
     */
    public void startAutoScroll() {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        // 先停止
        stopAutoScroll();
        if (null == timeHandler) {
            timeHandler = new Handler();
        }
        next(this.currentIndex);
    }

    /***
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        if (null == timeHandler) {
            return;
        }
        timeHandler.removeCallbacksAndMessages(null);
    }

    public void setUpDownListener(UpDownListener upDownListener) {
        this.upDownListener = upDownListener;
    }

    @Override
    public void onClick(View v) {
        if (upDownListener != null) {
            upDownListener.click();
        }
    }

    public interface UpDownListener {
        void click();
    }

    private class TimeRunnable implements Runnable {
        private int index = 0;
        private UpDownTimeTask timeTask;
        private WeakReference<Context> contextWeakReference;
        private List<String> mainBeansList;
        private int animMode;

        public TimeRunnable(UpDownTimeTask timeTask, Context context, List<String> mainBeansList, int animMode) {
            if (null == contextWeakReference) {
                contextWeakReference = new WeakReference<Context>(context);
            }
            this.timeTask = timeTask;
            if (null == this.mainBeansList) {
                this.mainBeansList = new ArrayList<>();
            } else {
                this.mainBeansList.clear();
            }
            if (null != mainBeansList && mainBeansList.size() > 0) {
                this.mainBeansList.addAll(mainBeansList);
            }
            this.animMode = animMode;
        }

        @Override
        public void run() {
            if (null == contextWeakReference || null == contextWeakReference.get()) {
                return;
            }
            if (null == this.timeTask) {
                return;
            }
            index = (index) % mainBeansList.size();
            switch (animMode) {
                case ANIM_MODE_UP:
                    setTextUpAnim(mainBeansList.get(index));
                    break;
                case ANIM_MODE_DOWN:
                    setTextDownAnim(mainBeansList.get(index));
                    break;
            }
            if (index >= mainBeansList.size()) {
                index = 0;
            }
            index++;

            this.timeTask.next(index);
        }
    }

    /***
     * 向上弹动画
     */
    public void setTextUpAnim(String text) {
        data = text;
        textViews[2].setText(text);
        up();// 向上的动画
    }

    public void setTextDownAnim(String text) {
        data = text;
        textViews[0].setText(text);
        down();// 向上的动画
    }

    public void setDuring(int during) {
        this.mAnimTime = during;
    }

    /***
     * 向上动画
     */
    private void up() {
        rootView.clearAnimation();
        if (animationUp == null)
            animationUp = new TranslateAnimation(0, 0, 0, -getHeight());
        animationUp.setDuration(mAnimTime);
        rootView.startAnimation(animationUp);
        animationUp.setAnimationListener(listener);
    }

    /***
     * 向下动画
     */
    public void down() {
        rootView.clearAnimation();
        if (animationDown == null)
            animationDown = new TranslateAnimation(0, 0, 0, getHeight());
        animationDown.setDuration(mAnimTime);
        rootView.startAnimation(animationDown);
        animationDown.setAnimationListener(listener);
    }

    /***
     * 动画监听，动画完成后，动画恢复，设置文本
     */
    private AnimationListener listener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) {
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            setText(data);
        }
    };

    public int getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(int mAnimTime) {
        this.mAnimTime = mAnimTime;
    }

    public int getStillTime() {
        return mStillTime;
    }

    public void setStillTime(int mStillTime) {
        this.mStillTime = mStillTime;
    }


    public void setData(List<String> dataList) {
        if (null == this.dataList) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList.clear();
        }
        this.dataList.addAll(dataList);
        if (this.dataList.size() > 0) {
            setText(this.dataList.get(0));
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getAnimMode() {
        return animMode;
    }

    public void setAnimMode(int animMode) {
        this.animMode = animMode;
    }

}
