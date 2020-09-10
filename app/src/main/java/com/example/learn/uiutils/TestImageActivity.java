package com.example.learn.uiutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.ui.titlebar.TitleBarView;
import com.example.learn.R;
import com.example.ui.circle.RoundImageView2;
import com.example.utils.download.DownloadUtil;
import com.example.utils.storage.FileUtil;
import com.example.utils.view.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ImageUtil测试类
 */
public class TestImageActivity extends BaseActivity {

    private TitleBarView testImageTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);

        testImageTitleBar = (TitleBarView) findViewById(R.id.test_image_title_bar);

    }

    public void viewToBitmap(View view) {
        Button testImageConvertBtn = (Button) findViewById(R.id.test_image_convert_btn);
        ImageView testImageConvertView = (ImageView) findViewById(R.id.test_image_convert_view);

        if (testImageConvertBtn.getText().equals("生成图片")) {
            testImageConvertBtn.setText("隐藏图片");

            ScrollView testImageConvertLayout = (ScrollView) findViewById(R.id.test_image_convert_layout);
            testImageConvertView.setVisibility(View.VISIBLE);

            String uriString = ImageUtil.viewToBitmap(testImageConvertLayout.getChildAt(0),
                    FileUtil.getDefaultFilePath(context), System.currentTimeMillis() + ".jpg");
            if (TextUtils.isEmpty(uriString)) {
                toast("uriString的值为null");
            } else {
                testImageConvertView.setImageURI(Uri.parse(uriString));
            }
        } else {
            testImageConvertBtn.setText("生成图片");
            testImageConvertView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示长图
     */
    public void testLongImageShow(View view) {
        Button testImageLongShowBtn = (Button) findViewById(R.id.test_image_long_show_btn);
        final LinearLayout testImageLongContainer = (LinearLayout) findViewById(R.id.test_image_long_container);

        if (testImageLongShowBtn.getText().equals("展示长图")) {
            testImageLongShowBtn.setText("隐藏长图");
            testImageLongContainer.setVisibility(View.VISIBLE);

            testImageLongContainer.removeAllViews();

            final List<String> list = new ArrayList<>();
            list.add("http://cdn1.showjoy.com/images/1d/1d1de746af2048cfb4e4687b384c3bf0.jpg");
            list.add("http://cdn1.showjoy.com/images/gif/loading2.gif");
            new DownloadUtil(context, FileUtil.getDefaultFilePath(context), list, new DownloadUtil.DownloadStateListener() {
                @Override
                public void onStart() {
                    toast("开始下载文件");
                }

                @Override
                public void onFailure(List<Integer> failureIndexs) {
                    toast("下载失败");
                }

                @Override
                public void onAllSucceed(final String fileDownloadDir) {
                    toast("下载成功，下载位置：" + fileDownloadDir);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (String url : list) {
                                try {
                                    File file = new File(fileDownloadDir, url.substring(url.lastIndexOf("/") + 1));

                                    //获得图片的宽、高
                                    BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
                                    tmpOptions.inJustDecodeBounds = true;
                                    BitmapFactory.decodeFile(file.getAbsolutePath(), tmpOptions);
                                    int width = tmpOptions.outWidth;
                                    int height = tmpOptions.outHeight;

                                    if (height < ImageUtil.getGLESTextureLimit()) {
                                        ImageView imageView = new ImageView(context);
                                        imageView.setAdjustViewBounds(true);
                                        imageView.setImageURI(Uri.parse(file.getAbsolutePath()));
                                        testImageLongContainer.addView(imageView);
                                        continue;
                                    }

                                    //设置显示图片的中心区域
                                    BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(file.getAbsolutePath(), false);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                                    int factor = height / ImageUtil.getGLESTextureLimit();
                                    for (int i = 0; i < factor; i++) {
                                        ImageView imageView = new ImageView(context);
                                        imageView.setAdjustViewBounds(true);
                                        imageView.setImageBitmap(bitmapRegionDecoder.decodeRegion(
                                                new Rect(0, (height / factor) * i, width, (height / factor) * (i + 1)), options));
                                        testImageLongContainer.addView(imageView);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }

                @Override
                public void onError(IOException e) {
                    toast("下载错误, 错误信息：" + e.getMessage());
                }
            }).startDownload();

        } else {
            testImageLongShowBtn.setText("展示长图");
            testImageLongContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 展示圆角图
     * 占用内存大约2MB左右
     */
    public void testRoundImageShow(View view) {
        Button testImageRoundShowBtn = (Button) findViewById(R.id.test_image_round_show_btn);
        RoundImageView2 testImageRoundView2 = (RoundImageView2) findViewById(R.id.test_image_round_view2);

        if (testImageRoundShowBtn.getText().equals("展示圆角图")) {
            testImageRoundShowBtn.setText("隐藏圆角图");
            testImageRoundView2.setVisibility(View.VISIBLE);
        } else {
            testImageRoundShowBtn.setText("展示圆角图");
            testImageRoundView2.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Bitmap的Options
     */
    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    @Override
    public TitleBarView getTitleBarView() {
        return testImageTitleBar;
    }

}
