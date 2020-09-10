package com.example.learn.uiutils;

import android.graphics.Matrix;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.base.BaseActivity;
import com.example.common.ui.titlebar.TitleBarView;
import com.example.learn.R;
import com.example.utils.device.DensityUtil;

/**
 * 测试其他Wiki的主要类
 */
public class TestOtherActivity extends BaseActivity {

    private TitleBarView testOtherTitleBar;

    // 测试Matrix图片时的缩放因子
    float factor = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_other);

        testOtherTitleBar = (TitleBarView) findViewById(R.id.test_other_title_bar);
    }

    /**
     * 格式化价格，样式1：￥12.34，目标：放大12的大小
     */
    public void testPriceFormat(View view) {
        TextView testFormatPrice = (TextView) findViewById(R.id.test_format_price);
        if ("测试价格格式化：￥12.34".equals(testFormatPrice.getText().toString().trim())) {
            ((Button) view).setText("取消");
            String tmp = "ok：" + testFormatPrice.getText().toString();
            SpannableString spannableString = new SpannableString(tmp);
            spannableString.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(context, 12)), tmp.indexOf("￥"), tmp.indexOf("￥") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(context, 24)), tmp.indexOf("￥") + 1, tmp.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(context, 12)), tmp.indexOf("."), tmp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            testFormatPrice.setText(spannableString);
        } else {
            ((Button) view).setText("执行");
            testFormatPrice.setText("测试价格格式化：￥12.34");
        }
    }

    /**
     * 测试Matrix图片缩小
     */
    public void testMatrixReduce(View view) {
        ImageView testImageScale = (ImageView) findViewById(R.id.test_image_scale);
        Matrix matrix = new Matrix();
        int width = testImageScale.getMeasuredWidth();
        int height = testImageScale.getMeasuredHeight();
        factor = factor - 0.1f;
        if (factor < 0.1) {
            factor = 0.1f;
            toast("已经缩小到最小");
        }
        matrix.postScale(factor, factor, width/2, height/2);
        testImageScale.setImageMatrix(matrix);
    }

    /**
     * 测试Matrix图片放大
     */
    public void testMatrixEnlarge(View view) {
        ImageView testImageScale = (ImageView) findViewById(R.id.test_image_scale);
        Matrix matrix = new Matrix();
        int width = testImageScale.getMeasuredWidth();
        int height = testImageScale.getMeasuredHeight();
        factor = factor + 0.1f;
        if (factor > 1f) {
            factor = 1f;
            toast("已经放大到最大");
        }
        matrix.postScale(factor, factor, width/2, height/2);
        testImageScale.setImageMatrix(matrix);
    }

    @Override
    public TitleBarView getTitleBarView() {
        return testOtherTitleBar;
    }

}
