package com.example.ui.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 参考文章：http://www.bkjia.com/Androidjc/947725.html
 *
 * 需要在XML中设置如下属性才可以生效
     android:ellipsize="marquee"
     android:singleLine="true"
 */
@SuppressLint("AppCompatCustomView")
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    private void init() {
        setSelected(true);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
