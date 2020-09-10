package com.example.common.ui.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.R;
import com.example.utils.device.DensityUtil;

/**
 * 自定义的圆角标签
 */
public class TagView extends LinearLayout {

    GradientDrawable gradientDrawable;
    float strokeWidth = 2;        // 标签外环宽度
    int strokeColor = Color.GRAY; // 标签外环颜色
    float radius = 50;            // 标签外环弧度
    int color = Color.WHITE;      // 标签背景颜色

    float paddingLeft = 15;       // 标签左右内边距
    float paddingTop = 5;         // 标签上下内边距

    TextView textView;
    String text = "";             // 标签文字内容
    float textSize = 12;          // 标签文字大小
    int textColor = Color.BLACK;  // 标签文字颜色

    public TagView(Context context) {
        super(context);
        init(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);

            strokeWidth = typeArray.getDimension(R.styleable.TagView_tag_stroke_width, strokeWidth);
            strokeColor = typeArray.getColor(R.styleable.TagView_tag_stroke_color, strokeColor);
            radius = typeArray.getDimension(R.styleable.TagView_tag_radius, radius);
            color = typeArray.getColor(R.styleable.TagView_tag_color, color);

            paddingLeft = typeArray.getDimension(R.styleable.TagView_tag_padding_left, paddingLeft);
            paddingTop = typeArray.getDimension(R.styleable.TagView_tag_padding_top, paddingTop);

            text = typeArray.getString(R.styleable.TagView_tag_text);
            textSize = typeArray.getDimension(R.styleable.TagView_tag_text_size, textSize);
            textColor = typeArray.getColor(R.styleable.TagView_tag_text_color, textColor);

            typeArray.recycle();
        }

        gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(DensityUtil.dp2px(context, strokeWidth), strokeColor);
        gradientDrawable.setCornerRadius(DensityUtil.dp2px(getContext(), radius));
        gradientDrawable.setColor(color);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        setBackgroundDrawable(gradientDrawable);

        setPadding(paddingLeft, paddingTop);

        textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(textColor);
        addView(textView);

        setGravity(Gravity.CENTER);
    }

    public TagView setStroke(int width, int color) {
        gradientDrawable.setStroke(DensityUtil.dp2px(getContext(), width), color);
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    public TagView setRadius(int radius) {
        gradientDrawable.setCornerRadius(DensityUtil.dp2px(getContext(), radius));
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    public TagView setColor(int color) {
        gradientDrawable.setColor(color);
        setBackgroundDrawable(gradientDrawable);
        return this;
    }

    public TagView setPadding(float paddingLet, float paddingTop) {
        int paddingLetTmp = DensityUtil.dp2px(getContext(), paddingLet);
        int paddingTopTmp = DensityUtil.dp2px(getContext(), paddingTop);
        setPadding(paddingLetTmp, paddingTopTmp, paddingLetTmp, paddingTopTmp);
        return this;
    }

    public String getText() {
        return textView.getText().toString();
    }

    public TagView setText(CharSequence text) {
        textView.setText(text);
        return this;
    }

    public TagView setTextSize(float size) {
        textView.setTextSize(size);
        return this;
    }

    public TagView setTextColor(int color) {
        textView.setTextColor(color);
        return this;
    }

}
