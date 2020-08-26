package com.example.learn.ui.partition.partition.two;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learn.R;
import com.example.learn.ui.partition.IPartitionCallback;

public class TwoView extends LinearLayout {

    private TextView twoViewText;

    public TwoView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public TwoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TwoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //
        inflate(context, R.layout.two_view, this);
        //
        twoViewText = (TextView) findViewById(R.id.two_view_text);
        //
        setOrientation(VERTICAL);
    }

    public void initData(final TwoBean.DataBean dataBean, final IPartitionCallback callback, final int position) {
        if (dataBean == null || callback == null) {
            return;
        }
        twoViewText.setText(dataBean.getText());
        twoViewText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.clickTwo(position, dataBean.getText());
            }
        });
    }
}
