package com.example.common.ui.titlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.example.common.R;
import com.example.common.ui.text.IconFontTextView;
import com.example.ui.text.MarqueeTextView;

/**
 * 统一的标题栏
 */
public class TitleBarView extends LinearLayout {

    // layout
    private LinearLayout titleBarRoot;
    private LinearLayout titleBarLeftIconContainer;
    private IconFontTextView titleBarLeftIcon;
    private TextView titleBarLeftText;
    private MarqueeTextView titleBarTitle;
    private TextView titleBarRightText;
    private LinearLayout titleBarRightIconContainer;
    private IconFontTextView titleBarRightIcon;
    private View titleBarLine;

    public TitleBarView(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_title_bar, this);

        titleBarRoot = (LinearLayout) findViewById(R.id.title_bar_root);
        titleBarLeftIconContainer = (LinearLayout) findViewById(R.id.title_bar_left_icon_container);
        titleBarLeftIcon = (IconFontTextView) findViewById(R.id.title_bar_left_icon);
        titleBarLeftText = (TextView) findViewById(R.id.title_bar_left_text);
        titleBarTitle = (MarqueeTextView) findViewById(R.id.title_bar_title);
        titleBarRightText = (TextView) findViewById(R.id.title_bar_right_text);
        titleBarRightIconContainer = (LinearLayout) findViewById(R.id.title_bar_right_icon_container);
        titleBarRightIcon = (IconFontTextView) findViewById(R.id.title_bar_right_icon);
        titleBarLine = (View) findViewById(R.id.title_bar_line);

        if (null != attrs) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

            setTitle(typeArray.getString(R.styleable.TitleBar_title_bar_title));
            setLineVisible(typeArray.getBoolean(R.styleable.TitleBar_title_bar_line, true));

            typeArray.recycle();
        }

    }

    public void setBackGroundColor(@ColorInt int color) {
        titleBarRoot.setBackgroundColor(color);
    }

    /**
     * left
     */

    public void setLeftTextIconColor(@ColorInt int color) {
        titleBarLeftText.setTextColor(color);
        titleBarLeftIcon.setTextColor(color);
    }

    public void setLeftTextIconSize(float size) {
        titleBarLeftText.setTextSize(size);
        titleBarLeftIcon.setTextSize(size);
    }

    public void setLeftIconText(String text) {
        titleBarLeftIcon.setText(text);
    }

    public void setLeftIconVisible(boolean visible) {
        setViewVisible(visible, titleBarLeftIconContainer, titleBarRightIconContainer);
    }

    public void setLeftIconClickListener(OnClickListener listener) {
        titleBarLeftIconContainer.setOnClickListener(listener);
    }

    public void setLeftTextText(String text) {
        titleBarLeftText.setText(text);
    }

    public void setLeftTextVisible(boolean visible) {
        setViewVisible(visible, titleBarLeftText, titleBarRightText);
    }

    public void setLeftTextClickListener(OnClickListener listener) {
        titleBarLeftText.setOnClickListener(listener);
    }

    /**
     * title
     */

    public void setTitle(String title) {
        titleBarTitle.setText(title);
    }

    public void setTitleColor(@ColorInt int color) {
        titleBarTitle.setTextColor(color);
    }

    public void setTitleSize(float size) {
        titleBarTitle.setTextSize(size);
    }

    /**
     * right
     */

    public void setRightTextIconColor(@ColorInt int color) {
        titleBarRightText.setTextColor(color);
        titleBarRightIcon.setTextColor(color);
    }

    public void setRightTextIconSize(float size) {
        titleBarRightText.setTextSize(size);
        titleBarRightIcon.setTextSize(size);
    }

    public void setRightTextText(String text) {
        titleBarRightText.setText(text);
    }

    public void setRightTextVisible(boolean visible) {
        setViewVisible(visible, titleBarRightText, titleBarLeftText);
    }

    public void setRightTextClickListener(OnClickListener listener) {
        titleBarRightText.setOnClickListener(listener);
    }

    public void setRightIconText(String text) {
        titleBarRightIcon.setText(text);
    }

    public void setRightIconVisible(boolean visible) {
        setViewVisible(visible, titleBarRightIconContainer, titleBarLeftIconContainer);
    }

    public void setRightIconClickListener(OnClickListener listener) {
        titleBarRightIconContainer.setOnClickListener(listener);
    }

    /**
     * line
     */

    public void setLineColor(@ColorInt int color) {
        if (titleBarLine.getVisibility() == View.VISIBLE) {
            titleBarLine.setBackgroundColor(color);
        }
    }

    public void setLineVisible(boolean visible) {
        if (visible) {
            titleBarLine.setVisibility(VISIBLE);
        } else {
            titleBarLine.setVisibility(GONE);
        }
    }

    /**
     * 目的：使titleBarTitle居中显示
     * @param visible 设置mainView是否显示
     * @param mainView 想要设置是否显示的View
     * @param changeView 根据mainView的显示状态而随之变化的View
     */
    private void setViewVisible(boolean visible, View mainView, View changeView) {
        if (visible) {
            mainView.setVisibility(View.VISIBLE);
            if (changeView.getVisibility() != View.VISIBLE) {
                changeView.setVisibility(View.INVISIBLE);
            }
        } else {
            if (changeView.getVisibility() == View.VISIBLE) {
                mainView.setVisibility(View.INVISIBLE);
            } else {
                mainView.setVisibility(View.GONE);
                changeView.setVisibility(View.GONE);
            }
        }
    }

}
