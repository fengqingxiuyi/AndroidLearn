package com.example.common.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.common.R;
import com.example.ui.dialog.DialogBaseFragment;
import com.example.utils.device.DeviceUtil;

import org.jetbrains.annotations.NotNull;

/**
 * 自定义的消息弹窗组件
 */
public class DialogMessageFragment extends DialogBaseFragment {

    private TextView viewDialogTitle;
    private TextView viewDialogContent;
    private Button viewDialogLeft;
    private Button viewDialogRight;
    // data
    private String title;
    private String content;
    private String leftText;
    private int leftTextColor;
    private String rightText;
    private int rightTextColor;
    // listener
    View.OnClickListener leftListener;
    View.OnClickListener rightListener;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        setSize((int) (DeviceUtil.getScreenWidth(context) * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getContentView() {
        return R.layout.view_dialog_message;
    }

    @Override
    public void initView(@NotNull View view) {
        viewDialogTitle = (TextView) findViewById(R.id.view_dialog_title);
        viewDialogContent = (TextView) findViewById(R.id.view_dialog_content);
        viewDialogLeft = (Button) findViewById(R.id.view_dialog_left);
        viewDialogRight = (Button) findViewById(R.id.view_dialog_right);

        initData();
    }

    private void initData() {
        setBottom(false);
        // title
        if (TextUtils.isEmpty(title)) {
            viewDialogTitle.setVisibility(View.GONE);
        } else {
            viewDialogTitle.setVisibility(View.VISIBLE);
            viewDialogTitle.setText(title);
        }
        // content
        viewDialogContent.setText(content);
        // left
        if (TextUtils.isEmpty(leftText)) {
            viewDialogLeft.setVisibility(View.GONE);
        } else {
            viewDialogLeft.setVisibility(View.VISIBLE);
            viewDialogLeft.setText(leftText);
            if (leftTextColor != 0) {
                viewDialogLeft.setTextColor(leftTextColor);
            }
        }
        // right
        if (TextUtils.isEmpty(rightText)) {
            viewDialogRight.setVisibility(View.GONE);
        } else {
            viewDialogRight.setVisibility(View.VISIBLE);
            viewDialogRight.setText(rightText);
            if (rightTextColor != 0) {
                viewDialogRight.setTextColor(rightTextColor);
            }
        }
        // listener
        viewDialogLeft.setOnClickListener(leftListener);
        viewDialogRight.setOnClickListener(rightListener);
    }

    public DialogMessageFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public DialogMessageFragment setContent(String content) {
        this.content = content;
        return this;
    }

    public DialogMessageFragment setLeftText(String leftText) {
        this.leftText = leftText;
        return this;
    }

    public DialogMessageFragment setLeftTextColor(int leftTextColor) {
        try {
            this.leftTextColor = mContext.getResources().getColor(leftTextColor);
        } catch (Exception e) {
            this.leftTextColor = 0xff000000;
        }
        return this;
    }

    public DialogMessageFragment setRightText(String rightText) {
        this.rightText = rightText;
        return this;
    }

    public DialogMessageFragment setRightTextColor(int rightTextColor) {
        try {
            this.rightTextColor = mContext.getResources().getColor(rightTextColor);
        } catch (Exception e) {
            this.rightTextColor = 0xff000000;
        }
        return this;
    }

    public DialogMessageFragment setLeftClickListener(View.OnClickListener leftListener) {
        this.leftListener = leftListener;
        return this;
    }

    public DialogMessageFragment setRightClickListener(View.OnClickListener rightListener) {
        this.rightListener = rightListener;
        return this;
    }

}
