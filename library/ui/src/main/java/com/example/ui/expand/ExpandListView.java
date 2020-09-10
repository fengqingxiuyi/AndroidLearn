package com.example.ui.expand;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 解决ScrollView嵌套ListView，导致ListView显示不全的问题
 */
public class ExpandListView extends ListView {

    public ExpandListView(Context context) {
        super(context);
    }

    public ExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
