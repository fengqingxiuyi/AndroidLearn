package com.example.ui.toast;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.ui.R;

public class ToastView extends FrameLayout {

    private ImageView toastFlag;
    private TextView toastMsg;

    public ToastView(Context context) {
        super(context);
        init(context, null);
    }

    public ToastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToastView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.uikit_toast_view, this);

        toastFlag = (ImageView) findViewById(R.id.toast_flag);
        toastMsg = (TextView) findViewById(R.id.toast_msg);
    }

    public void setText(String text) {
        toastMsg.setText(text);
    }

    public void setToastType(int type) {
        if (type == ToastType.TYPE_SUCCESS) {
            toastFlag.setVisibility(View.VISIBLE);
            toastFlag.setImageResource(R.drawable.toast_success);
        } else if (type == ToastType.TYPE_ERROR) {
            toastFlag.setVisibility(View.VISIBLE);
            toastFlag.setImageResource(R.drawable.toast_error);
        } else {
            toastFlag.setVisibility(View.GONE);
        }
    }
}

