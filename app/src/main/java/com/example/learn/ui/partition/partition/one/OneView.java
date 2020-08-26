package com.example.learn.ui.partition.partition.one;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learn.R;
import com.example.learn.ui.partition.IPartitionCallback;

public class OneView extends LinearLayout {

    private TextView oneViewText;

    public OneView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public OneView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OneView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //
        inflate(context, R.layout.one_view, this);
        //
        oneViewText = (TextView) findViewById(R.id.one_view_text);
        //
        setOrientation(VERTICAL);
    }

    public void initData(final OneBean.DataBean dataBean, final IPartitionCallback callback, final int position) {
        if (dataBean == null || callback == null) {
            return;
        }
        oneViewText.setText(dataBean.getText());
        oneViewText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.clickOne(position, dataBean.getText());
            }
        });
    }
}
