package com.example.ui.expand;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决ScrollView嵌套GridView，导致GridView显示不全的问题
 */
public class ExpandGridView extends GridView {

    public ExpandGridView(Context context) {
        super(context);
    }

    public ExpandGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
