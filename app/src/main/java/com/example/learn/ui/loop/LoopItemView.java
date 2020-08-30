package com.example.learn.ui.loop;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learn.R;

import java.util.List;

public class LoopItemView<T> extends FrameLayout {

    private TextView viewItemText;

    private List<T> dataList;

    public LoopItemView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public LoopItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoopItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.loop_view_item, this);

        viewItemText = (TextView) findViewById(R.id.view_item_text);

    }

    public void setData(List<T> dataList) {
        this.dataList = dataList;
    }

    public List<T> getData() {
        return dataList;
    }

    public void setText(String text) {
        viewItemText.setText(text);
    }

}
