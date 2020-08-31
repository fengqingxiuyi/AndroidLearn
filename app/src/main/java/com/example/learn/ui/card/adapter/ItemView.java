package com.example.learn.ui.card.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learn.R;

public class ItemView extends FrameLayout {

    private TextView itemText;

    public ItemView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        inflate(context, R.layout.activity_card_view_item, this);

        itemText = (TextView) findViewById(R.id.item_text);

    }

    public void initData(String s) {
        itemText.setText(s);
    }
}
