package com.example.banner.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import androidx.annotation.NonNull;

/**
 * @author fqxyi
 * @date 2017/9/1
 * 本地、网络图片加载
 */
public class DefaultImageHolderView<T> implements Holder<T> {

    private int bannerHeight;
    private int bannerBgColor;

    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

    // 显示图片事件
    private ImageShowListener<T> imageShowListener;

    public interface ImageShowListener<T> {
        void showImage(ImageView imageView, T data);
    }

    public void setImageShowListener(ImageShowListener<T> imageShowListener) {
        this.imageShowListener = imageShowListener;
    }

    // 点击事件
    private ImageClickListener<T> clickListener;

    public interface ImageClickListener<T> {
        void click(View view, int position, T data);
    }

    public void setClickListener(ImageClickListener<T> clickListener) {
        this.clickListener = clickListener;
    }

    // 长按事件
    private ImageLongClickListener<T> longListener;

    public interface ImageLongClickListener<T> {
        void longClick(View view, int position, T data);
    }

    public void setLongClickListener(ImageLongClickListener<T> longListener) {
        this.longListener = longListener;
    }

    public DefaultImageHolderView() {
        this.bannerHeight = LayoutParams.MATCH_PARENT;
    }

    public DefaultImageHolderView(int bannerHeight) {
        this.bannerHeight = bannerHeight;
    }

    /**
     * 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
     */
    @Override
    public View createView(Context context) {
        ImageView imageView = new ImageView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        imageView.setLayoutParams(params);
        imageView.setScaleType(scaleType);

        if (bannerBgColor != 0) {
            imageView.setBackgroundColor(bannerBgColor);
        }
        return imageView;
    }

    @Override
    public void updateUI(Context context, final View view, final int position, @NonNull final T data) {
        if (view instanceof ImageView && imageShowListener != null) {
            imageShowListener.showImage((ImageView) view, data);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != clickListener) {
                    clickListener.click(view, position, data);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != longListener) {
                    longListener.longClick(view, position, data);
                }
                return false;
            }
        });
    }

    public void setBannerHeight(int bannerHeight) {
        this.bannerHeight = bannerHeight;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public void setBannerBgColor(int color) {
        this.bannerBgColor = color;
    }
}
