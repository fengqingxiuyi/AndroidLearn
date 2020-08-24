package com.example.ui.toast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ui.R;

public class ToastDialog extends Dialog {

    private Context context;

    public ToastDialog(@NonNull Context context) {
        super(context, R.style.uikit_transparent_style);
        this.context = context;
        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        if (params == null) {
            return;
        }
        //这条就是控制点击背景的时候  如果被覆盖的view有点击事件那么就会直接触发(dialog消失并且触发背景下面view的点击事件)
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        window.setAttributes(params);
    }

    @Nullable
    public Activity getActivity() {
        if (context instanceof Activity) {
            return (Activity) context;
        } else {
            return null;
        }
    }

}
