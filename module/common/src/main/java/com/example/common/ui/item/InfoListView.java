package com.example.common.ui.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.R;
import com.example.common.ui.text.IconFontTextView;

/**
 * 常用的信息列表View
 */
public class InfoListView extends LinearLayout {

    /** layout **/
    private View viewInfoListRoot;
    private IconFontTextView viewInfoListLeftIcon;
    private TextView viewInfoListLeftText;
    private TextView viewInfoListRightText;
    private IconFontTextView viewInfoListRightIcon;

    /** data default **/
    // bg color
    private int bgColor = Color.WHITE;
    // left icon
    private String leftIcon = "";
    private float leftIconSize = 16;
    private int leftIconColor = Color.BLACK;
    // left text
    private String leftText = "";
    private float leftTextSize = 14;
    private int leftTextColor = Color.BLACK;
    // right text
    private String rightText = "";
    private float rightTextSize = 12;
    private int rightTextColor = Color.BLACK;
    // right icon
    private String rightIcon = "";
    private float rightIconSize = 16;
    private int rightIconColor = Color.BLACK;

    public InfoListView(Context context) {
        super(context);
        init(context, null);
    }

    public InfoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InfoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_info_list, this);

        viewInfoListRoot = findViewById(R.id.view_info_list_root);
        viewInfoListLeftIcon = (IconFontTextView) findViewById(R.id.view_info_list_left_icon);
        viewInfoListLeftText = (TextView) findViewById(R.id.view_info_list_left_text);
        viewInfoListRightText = (TextView) findViewById(R.id.view_info_list_right_text);
        viewInfoListRightIcon = (IconFontTextView) findViewById(R.id.view_info_list_right_icon);

        /** get data from layout **/

        if (null != attrs) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.InfoListView);

            bgColor = typeArray.getColor(R.styleable.InfoListView_bg_color, bgColor);
            // left icon
            leftIcon = typeArray.getString(R.styleable.InfoListView_left_icon);
            leftIconSize = typeArray.getDimension(R.styleable.InfoListView_left_icon_size, leftIconSize);
            leftIconColor = typeArray.getColor(R.styleable.InfoListView_left_icon_color, leftIconColor);
            // left text
            leftText = typeArray.getString(R.styleable.InfoListView_left_text);
            leftTextSize = typeArray.getDimension(R.styleable.InfoListView_left_text_size, leftTextSize);
            leftTextColor = typeArray.getColor(R.styleable.InfoListView_left_text_color, leftTextColor);
            // right text
            rightText = typeArray.getString(R.styleable.InfoListView_right_text);
            rightTextSize = typeArray.getDimension(R.styleable.InfoListView_right_text_size, rightTextSize);
            rightTextColor = typeArray.getColor(R.styleable.InfoListView_right_text_color, rightTextColor);
            // right icon
            rightIcon = typeArray.getString(R.styleable.InfoListView_right_icon);
            rightIconSize = typeArray.getDimension(R.styleable.InfoListView_right_icon_size, rightIconSize);
            rightIconColor = typeArray.getColor(R.styleable.InfoListView_right_icon_color, rightIconColor);

            typeArray.recycle();
        }

        /** set data of layout **/

        viewInfoListRoot.setBackgroundColor(bgColor);
        // left icon
        if (TextUtils.isEmpty(leftIcon)) {
            viewInfoListLeftIcon.setVisibility(View.GONE);
        } else {
            viewInfoListLeftIcon.setVisibility(View.VISIBLE);
            viewInfoListLeftIcon.setText(leftIcon);
            viewInfoListLeftIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftIconSize);
            viewInfoListLeftIcon.setTextColor(leftIconColor);
        }
        // left text
        if (!TextUtils.isEmpty(leftText)) {
            viewInfoListLeftText.setText(leftText);
            viewInfoListLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
            viewInfoListLeftText.setTextColor(leftTextColor);
        }
        // right text
        if (!TextUtils.isEmpty(rightText)) {
            viewInfoListRightText.setText(rightText);
            viewInfoListRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            viewInfoListRightText.setTextColor(rightTextColor);
        }
        // right icon
        if (TextUtils.isEmpty(rightIcon)) {
            viewInfoListRightIcon.setVisibility(View.GONE);
        } else {
            viewInfoListRightIcon.setVisibility(View.VISIBLE);
            viewInfoListRightIcon.setText(rightIcon);
            viewInfoListRightIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightIconSize);
            viewInfoListRightIcon.setTextColor(rightIconColor);
        }

    }

    public void setBgColor(int bgColor) {
        viewInfoListRoot.setBackgroundColor(bgColor);
    }

    public void setLeftIcon(String leftIcon) {
        viewInfoListLeftIcon.setText(leftIcon);
    }

    public void setLeftIconSize(float leftIconSize) {
        viewInfoListLeftIcon.setTextSize(leftIconSize);
    }

    public void setLeftIconColor(int leftIconColor) {
        viewInfoListLeftIcon.setTextColor(leftIconColor);
    }

    public void setLeftIconVisibility(int leftIconVisibility) {
        viewInfoListLeftIcon.setVisibility(leftIconVisibility);
    }

    public void setLeftText(String leftText) {
        viewInfoListLeftText.setText(leftText);
    }

    public void setLeftTextSize(float leftTextSize) {
        viewInfoListLeftText.setTextSize(leftTextSize);
    }

    public void setLeftTextColor(int leftTextColor) {
        viewInfoListLeftText.setTextColor(leftTextColor);
    }

    public void setRightText(String rightText) {
        viewInfoListRightText.setText(rightText);
    }

    public void setRightTextSize(float rightTextSize) {
        viewInfoListRightText.setTextSize(rightTextSize);
    }

    public void setRightTextColor(int rightTextColor) {
        viewInfoListRightText.setTextColor(rightTextColor);
    }

    public void setRightIcon(String rightIcon) {
        viewInfoListRightIcon.setText(rightIcon);
    }

    public void setRightIconSize(float rightIconSize) {
        viewInfoListRightIcon.setTextSize(rightIconSize);
    }

    public void setRightIconColor(int rightIconColor) {
        viewInfoListRightIcon.setTextColor(rightIconColor);
    }

    public void setRightIconVisibility(int rightIconVisibility) {
        viewInfoListRightIcon.setVisibility(rightIconVisibility);
    }

    /**
     * 设置字体
     */
    public void setTextTypeFace(Typeface tf) {
        viewInfoListLeftText.setTypeface(tf);
        viewInfoListRightText.setTypeface(tf);
    }

}
