package com.example.learn.ui.viewswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learn.R;

/**
 * @author fqxyi
 * @date 2018/3/23
 */
public class ViewSwitcherItemView extends FrameLayout {

    private TextView title;
    private TextView content;

    public ViewSwitcherItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ViewSwitcherItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewSwitcherItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.activity_view_switcher_item, this);

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);

    }

    public void initData(ViewSwitcherBean viewSwitcherBean) {
        if (viewSwitcherBean == null) {
            return;
        }
        title.setText(viewSwitcherBean.title);
        content.setText(viewSwitcherBean.content);
    }

}
