package com.example.ui.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.ui.R;
import com.example.ui.utils.DensityUtil;

/**
 * 圆形View，可用作：空心圆、实心圆、轮播图指示点等
 * 注意点：
 *       ① 建议设置layout_width和layout_height的值为wrap_content，否则可能导致View形状显示异常
 *       ② View自身会根据text内容的变化而变化
 *       ③ 仅允许在text内容为空时设置View的大小，即{@link #setSize(int)}方法
 */
public class CircleView extends LinearLayout {

    public enum CircleType {
        SOLID, RING, RING_DASH, NORMAL, NORMAL_DASH
    }

    private GradientDrawable gradientDrawable;
    private int strokeWidth = DensityUtil.dp2px(getContext(), 2); // 标签外环宽度
    private int strokeColor = Color.GRAY; // 标签外环颜色
    private int color = Color.WHITE; // 标签背景颜色

    private int tempStrokeWidth = DensityUtil.dp2px(getContext(), 2); // 暂存外部设置的strokeWidth
    private int tempStrokeColor = Color.GRAY; // 暂存外部设置的strokeColor
    private int tempColor = Color.WHITE; // 暂存外部设置的color
    private int tempDashWidth = DensityUtil.dp2px(getContext(), 2); // 暂存外部设置的dashWidth
    private int tempDashGap = DensityUtil.dp2px(getContext(), 2); // 暂存外部设置的dashGap

    private TextView textView;
    private String text = ""; // 标签文字内容
    private float textSize = 12; // 标签文字大小
    private int textColor = Color.BLACK; // 标签文字颜色

    int textMaxLength;
    int viewGroupMinLength;

    public CircleView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);

            strokeWidth = (int) typeArray.getDimension(R.styleable.CircleView_stroke_width, strokeWidth);
            strokeColor = typeArray.getColor(R.styleable.CircleView_stroke_color, strokeColor);
            color = typeArray.getColor(R.styleable.CircleView_color, color);

            text = typeArray.getString(R.styleable.CircleView_text);
            textSize = typeArray.getDimension(R.styleable.CircleView_text_size, textSize);
            textColor = typeArray.getColor(R.styleable.CircleView_text_color, textColor);

            typeArray.recycle();
        }

        setGravity(Gravity.CENTER);

        textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        addView(textView);

        textView.post(new Runnable() {
            @Override
            public void run() {
                if (textView.getMeasuredWidth() > textView.getMeasuredHeight()) {
                    textMaxLength = textView.getMeasuredWidth();
                } else {
                    textMaxLength = textView.getMeasuredHeight();
                }

                if (!TextUtils.isEmpty(textView.getText())) {
                    textMaxLength += DensityUtil.dp2px(context, 10);
                }

                gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(strokeWidth, strokeColor);
                gradientDrawable.setColor(color);
                gradientDrawable.setSize(textMaxLength, textMaxLength);
                gradientDrawable.setShape(GradientDrawable.OVAL);
                setBackgroundDrawable(gradientDrawable);
            }
        });

    }

    public String getText() {
        return textView.getText().toString().trim();
    }

    public CircleView setText(CharSequence text) {
        textView.setText(text);
        updateSize();
        return this;
    }

    public CircleView setTextSize(float size) {
        textView.setTextSize(size);
        updateSize();
        return this;
    }

    public CircleView setTextColor(int color) {
        textView.setTextColor(color);
        return this;
    }

    public CircleView setStroke(int width, int color) {
        tempStrokeWidth = DensityUtil.dp2px(getContext(), width);
        tempStrokeColor = color;
        gradientDrawable.setStroke(tempStrokeWidth, color);
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    public CircleView setStroke(int width, int color, float dashWidth, float dashGap) {
        tempStrokeWidth = DensityUtil.dp2px(getContext(), width);
        tempStrokeColor = color;
        tempDashWidth = DensityUtil.dp2px(getContext(), dashWidth);
        tempDashGap = DensityUtil.dp2px(getContext(), dashGap);
        gradientDrawable.setStroke(tempStrokeWidth, color, tempDashWidth, tempDashGap);
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    public CircleView setColor(int color) {
        tempColor = color;
        gradientDrawable.setColor(color);
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    /**
     * 无文本内容是使用
     */
    public CircleView setSize(int size) {
        if (TextUtils.isEmpty(textView.getText())) {
            size = DensityUtil.dp2px(getContext(), size);
            gradientDrawable.setSize(size, size);
            setBackgroundDrawable(gradientDrawable);
            // 大小改变，重绘布局
            requestLayout();
        }
        return this;
    }

    public CircleView setType(CircleType type) {
        if (CircleType.SOLID == type) {
            gradientDrawable.setStroke(0, Color.TRANSPARENT);
            gradientDrawable.setColor(tempColor);
        } else if (CircleType.RING == type) {
            gradientDrawable.setStroke(tempStrokeWidth, tempStrokeColor);
            gradientDrawable.setColor(Color.TRANSPARENT);
        } else if (CircleType.RING_DASH == type) {
            gradientDrawable.setStroke(tempStrokeWidth, tempStrokeColor, tempDashWidth, tempDashGap);
            gradientDrawable.setColor(Color.TRANSPARENT);
        } else if (CircleType.NORMAL == type) {
            gradientDrawable.setStroke(tempStrokeWidth, tempStrokeColor);
            gradientDrawable.setColor(tempColor);
        } else if (CircleType.NORMAL_DASH == type) {
            gradientDrawable.setStroke(tempStrokeWidth, tempStrokeColor, tempDashWidth, tempDashGap);
            gradientDrawable.setColor(tempColor);
        }
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    private void updateSize() {
        textView.post(new Runnable() {
            @Override
            public void run() {

                if (textView.getMeasuredWidth() > textView.getMeasuredHeight()) {
                    textMaxLength = textView.getMeasuredWidth();
                } else {
                    textMaxLength = textView.getMeasuredHeight();
                }

                if (!TextUtils.isEmpty(textView.getText())) {
                    textMaxLength += DensityUtil.dp2px(getContext(), 10);
                }

                gradientDrawable.setSize(textMaxLength, textMaxLength);
                setBackgroundDrawable(gradientDrawable);
                // 大小改变，重绘布局
                requestLayout();
            }
        });
    }

}
