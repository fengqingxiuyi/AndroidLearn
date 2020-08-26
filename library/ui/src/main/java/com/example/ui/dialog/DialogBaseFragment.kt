package com.example.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ui.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author fqxyi
 * @date 2020/8/20
 * 最基本的弹窗组件
 */
public abstract class DialogBaseFragment extends DialogFragment {

    protected Activity activity;
    protected Context context;
    protected View rootView;

    private boolean bottom = true;
    private boolean fromBottom = true;

    int width = ViewGroup.LayoutParams.MATCH_PARENT;
    int height = ViewGroup.LayoutParams.WRAP_CONTENT;

    private boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;

    private boolean isShowing = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        this.context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return null;
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(context, getContentView(), null);

        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

        initView(rootView);
        return rootView;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        this.cancelable = cancelable;
    }

    @Override
    public boolean isCancelable() {
        return cancelable;
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

    public boolean isCanceledOnTouchOutside() {
        return canceledOnTouchOutside;
    }

    public abstract @LayoutRes
    int getContentView();
    public abstract void initView(View view);

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();

        if (null == window) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();

        if (bottom) {
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
        if (fromBottom) {
            window.setWindowAnimations(R.style.BaseDialog_Anim);
        }

        window.setLayout(width, height);
    }

    @Override
    public void onDismiss(@NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isShowing = false;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public void setFromBottom(boolean fromBottom) {
        this.fromBottom = fromBottom;
    }

    public void show(FragmentActivity activity) {
        if (isShowing) {
            return;
        }

        isShowing = true;
        try {
            if (isAdded()) {
                return;
            }
            if (activity.isFinishing()) {
                return;
            }

            show(activity.getSupportFragmentManager(), getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected View findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

}
