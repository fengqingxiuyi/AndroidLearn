package com.example.learn.ui.image;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.image.FCircleImageView;
import com.example.image.FImageView;
import com.example.image.listener.IImageLoadListener;
import com.example.learn.R;

public class ImageActivity extends AppCompatActivity {

    private FImageView nullView;
    private FImageView localView;
    private FImageView wrapView;
    private FImageView netView;
    private FImageView gifView;
    private FImageView circleView;
    private FCircleImageView circleBorderView;
    private FImageView roundView;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        nullView = (FImageView) findViewById(R.id.null_view);
        localView = (FImageView) findViewById(R.id.local_view);
        wrapView = (FImageView) findViewById(R.id.wrap_view);
        netView = (FImageView) findViewById(R.id.net_view);
        gifView = (FImageView) findViewById(R.id.gif_view);
        circleView = (FImageView) findViewById(R.id.circle_view);
        circleBorderView = (FCircleImageView) findViewById(R.id.circle_border_view);
        roundView = (FImageView) findViewById(R.id.round_view);

        // 空值
        nullView.setImageUrl(url);
        // 本地静态图
        localView.setImageUrl(R.mipmap.local_png);
        // 宽高值为wrap_content的图片
        wrapView.setEnableWrapContent(true);
        wrapView.setImageUrl(R.mipmap.local_png, new IImageLoadListener() {
            @Override
            public void onSuccess(String id, int width, int height) {
                toast("图片加载成功");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                toast(String.format("图片加载失败: %s", throwable));
            }
        });
        // 网络静态图
        url = "http://pics.sc.chinaz.com/files/pic/pic9/201801/bpic5343.jpg";
        netView.setImageUrl(url, 50, 50); // 此时图片会很模糊
        // gif图
        url = "http://s1.dwstatic.com/group1/M00/9E/D2/d47dade8a5e62a9572b3993a94bef43b.gif";
        gifView.setImageUrl(url);
        // 圆形图
        url = "http://pics.sc.chinaz.com/files/pic/pic9/201801/bpic5343.jpg";
        circleView.setImageCircle();
        circleView.setImageUrl(url);
        // 圆形图 带边框
        url = "http://pics.sc.chinaz.com/files/pic/pic9/201801/bpic5343.jpg";
        circleBorderView.setBorderColor(Color.BLUE);
        circleBorderView.setBorderWidth(10);
        circleBorderView.setImageUrl(url);
        // 圆角图
        url = "http://pics.sc.chinaz.com/files/pic/pic9/201801/bpic5343.jpg";
        roundView.setImageRound(20);
        roundView.setImageUrl(url);

    }

    public void toast(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
