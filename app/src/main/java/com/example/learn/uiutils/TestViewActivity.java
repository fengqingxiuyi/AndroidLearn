package com.example.learn.uiutils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.common.base.BaseActivity;
import com.example.common.ui.tag.TagView;
import com.example.common.ui.text.IconFontTextView;
import com.example.common.ui.titlebar.TitleBarView;
import com.example.learn.R;
import com.example.ui.circle.CircleView;
import com.example.ui.container.AutoWrappedViewGroup;
import com.example.ui.drag.SingleTouchView;
import com.example.ui.toast.ToastUtil;

/**
 * 测试View组件的主要类
 */
public class TestViewActivity extends BaseActivity {

    private TitleBarView testViewTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);

        testViewTitleBar = (TitleBarView) findViewById(R.id.test_view_title_bar);

    }

    /**
     * 测试IconFont
     */
    public void testIconFont(View view) {
        IconFontTextView testIconFont = (IconFontTextView) findViewById(R.id.test_icon_font);
        testIconFont.setIconText("显示IconFont：<font color='#ff0000'>&#xe6f5;</font>");
        testIconFont.setTextSize(18);
    }

    /**
     * 测试自动换行容器
     */
    public void testWrappedGroup(View view) {
        Button testWrappedGroupBtn = (Button) findViewById(R.id.test_wrapped_group_btn);
        AutoWrappedViewGroup testWrappedGroup = (AutoWrappedViewGroup) findViewById(R.id.test_wrapped_group);
        if (testWrappedGroupBtn.getText().equals("测试自动换行容器-显示")) {
            testWrappedGroupBtn.setText("测试自动换行容器-隐藏");
            testWrappedGroup.setVisibility(View.VISIBLE);
            // 清除所有子View
            testWrappedGroup.removeAllViews();
            // 添加子View
            for (int i = 0; i < 3; i++) {
                TagView tagView = new TagView(context);
                tagView.setText("测试标签内容");
                tagView.setTextSize(15);
                tagView.setTextColor(Color.WHITE);
                tagView.setStroke(3, Color.GRAY);
                tagView.setColor(Color.GREEN);
                tagView.setPadding(10, 3);
                tagView.setRadius(50);
                testWrappedGroup.addView(tagView);
            }
        } else {
            testWrappedGroupBtn.setText("测试自动换行容器-显示");
            testWrappedGroup.setVisibility(View.GONE);
        }
    }

    /**
     * 测试圆形View
     */
    public void testCircleView(View view) {
        CircleView circleView = (CircleView) findViewById(R.id.test_circle_view);
        circleView.setText("1/10")
                .setTextSize(20)
                .setTextColor(Color.WHITE)
                .setColor(Color.BLUE)
                .setStroke(3, Color.RED, 5, 3)
                .setType(CircleView.CircleType.NORMAL_DASH);
    }

    /**
     * 测试可单手操作的图片
     */
    public void testSingleTouch(View view) {
        SingleTouchView singleTouchView = (SingleTouchView) findViewById(R.id.view_single_touch);
        singleTouchView.setImageResource(R.mipmap.ic_launcher);

        Button singleTouchBtn = (Button) findViewById(R.id.test_single_touch_btn);
        if (singleTouchBtn.getText().equals("显示可单手操作的图片")) {
            singleTouchBtn.setText("隐藏可单手操作的图片");
            singleTouchView.setVisibility(View.VISIBLE);
        } else {
            singleTouchBtn.setText("显示可单手操作的图片");
            singleTouchView.setVisibility(View.GONE);
        }
    }

    /**
     * 测试可拖拽View的点击事件
     */
    public void testViewDrag(View view) {
        ToastUtil.toast("点击了拖拽View");
    }

    @Override
    public TitleBarView getTitleBarView() {
        return testViewTitleBar;
    }

}
