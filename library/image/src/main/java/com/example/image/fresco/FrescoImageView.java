package com.example.image.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.image.listener.IImageLoadListener;
import com.example.image.listener.IImageView;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import okhttp3.OkHttpClient;

/**
 * @author fqxyi
 * @date 2018/1/27
 * FrescoImageView
 * 注意点:
 * 1、设置图片宽高，受actualImageScaleType属性值的限制，详见{@link ScalingUtils}
 * 参考资料：
 * 1、https://www.fresco-cn.org/docs/getting-started.html
 * 2、http://blog.csdn.net/chwnpp2/article/details/51063492
 */
public class FrescoImageView extends SimpleDraweeView implements IImageView {

    GenericDraweeHierarchy hierarchy;

    IImageLoadListener loadListener;

    boolean enableWrapContent = false;

    String imageUrl;
    int imageRes;

    public FrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public FrescoImageView(Context context) {
        super(context);
        init();
    }

    public FrescoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        hierarchy = getHierarchy();
        if (hierarchy == null) {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            hierarchy = builder.build();
            setHierarchy(hierarchy);
        }
    }

    public static void init(Context context) {
        init(context, null);
    }

    /**
     * Fresco 初始化
     *
     * 注意点：
     * 1、setDownsampleEnabled：必须和ImageRequest的ResizeOptions一起使用，
     * 作用就是在图片解码时根据ResizeOptions所设的宽高的像素进行解码，这样解码出来可以得到一个更小的Bitmap。
     * ResizeOptions和DownsampleEnabled参数都不影响原图片的大小，影响的是EncodeImage的大小，进而影响Decode出来的Bitmap的大小，
     * ResizeOptions须和此参数结合使用是因为单独使用ResizeOptions的话只支持JPEG图，所以需支持png、jpg、webp需要先设置此参数。
     */
    public static void init(Context context, OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            ImagePipelineConfig config = ImagePipelineConfig
                    .newBuilder(context)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(context, config);
            return;
        }
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, okHttpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(context, config);
    }

    /************ 图片属性 ************/

    /**
     * 设置为圆圈图
     */
    @Override
    public void setImageCircle() {
        hierarchy.setRoundingParams(RoundingParams.asCircle());
    }

    /**
     * 设置为圆角图
     * @param radius 圆角度数 单位：像素
     */
    @Override
    public void setImageRound(float radius) {
        hierarchy.setRoundingParams(RoundingParams.fromCornersRadius(radius));
    }

    /**
     * 设置图片自适应宽高
     * 适用场景：宽和高 都可以是 WRAP_CONTENT，但是不能为 0
     *
     * @param enableWrapContent 是否自适应
     */
    @Override
    public void setEnableWrapContent(boolean enableWrapContent) {
        this.enableWrapContent = enableWrapContent;
    }

    /************ 缓存 ************/

    /**
     * 清除内存缓存
     */
    public static void clearMemoryCaches() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清除磁盘缓存
     */
    public static void clearDiskCaches() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /**
     * 清除所有缓存
     */
    public static void clearCaches() {
        clearMemoryCaches();
        clearDiskCaches();
    }

    /************ 占位图 ************/

    /**
     * 设置占位图
     * @param resource 图片资源ID
     */
    @Override
    public void setPlaceholderImage(int resource) {
        hierarchy.setPlaceholderImage(resource);
    }

    /**
     * 设置占位图
     * @param resource 图片资源ID
     * @param scaleType 图片缩放类型
     */
    @Override
    public void setPlaceholderImage(int resource, ScalingUtils.ScaleType scaleType) {
        hierarchy.setPlaceholderImage(resource, scaleType);
    }

    /**
     * 设置占位图
     * @param drawable 图片资源
     */
    @Override
    public void setPlaceholderImage(Drawable drawable) {
        hierarchy.setPlaceholderImage(drawable);
    }

    /**
     * 设置占位图
     * @param drawable 图片资源
     * @param scaleType 图片缩放类型
     */
    @Override
    public void setPlaceholderImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        hierarchy.setPlaceholderImage(drawable, scaleType);
    }

    /************ 图片地址 ************/

    /**
     * 设置本地图片
     * @param resId 本地图片资源ID
     */
    @Override
    public void setImageUrl(int resId) {
        setImageUrl(resId, null);
    }

    /**
     * 设置本地图片
     * @param resId 本地图片资源ID
     * @param loadListener 图片加载监听器
     */
    @Override
    public void setImageUrl(int resId, IImageLoadListener loadListener) {
        this.loadListener = loadListener;
        setImageUrl(resId, 0, 0);
    }

    /**
     * 设置本地图片
     * @param resId 本地图片资源ID
     * @param width 图片宽度 单位：像素
     * @param height 图片高度 单位：像素
     */
    @Override
    public void setImageUrl(int resId, int width, int height) {
        imageRes = resId;
        setImageUrl("res://" + getContext().getPackageName() +  "/" + resId, width, height);
    }

    /**
     * 设置网络图片
     * @param url 网络图片地址
     */
    @Override
    public void setImageUrl(String url) {
        setImageUrl(url, null);
    }

    /**
     * 设置网络图片
     * @param url 网络图片地址
     * @param loadListener 图片加载监听器
     */
    @Override
    public void setImageUrl(String url, IImageLoadListener loadListener) {
        this.loadListener = loadListener;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (null != params) {
            int width = params.width;
            int height = params.height;
            if (width > 0 && height > 0) {
                setImageUrl(url, width, height);
                return;
            }
        }
        setImageUrl(url, 0, 0);
    }

    /**
     * 设置网络图片
     * @param url 网络图片地址
     */
    @Override
    public void setImageUrl(String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.startsWith("//")) {
            url = "http:" + url;
        }
        imageUrl = url;
        setController(getDraweeController(url, width, height));
    }

    /************ Other ************/

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int getImageRes() {
        return imageRes;
    }

    /**
     * 图片加载 最终实现类
     */
    private DraweeController getDraweeController(String url, int width, int height) {
        Uri uri = Uri.parse(url);
        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(postprocessor);
        if (width > 0 && height > 0) {
            /*
             * Resizing has some limitations:
             *      it only supports JPEG files
             *      the actual resize is carried out to the nearest 1/8 of the original size
             *      it cannot make your image bigger, only smaller (not a real limitation though)
             */
            requestBuilder.setResizeOptions(new ResizeOptions(width, height));
        }

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        if (url.endsWith(".gif")) {
            builder.setAutoPlayAnimations(true);
        }
        return builder.setImageRequest(requestBuilder.build())
                .setOldController(getController()) // ResizeOptions需要
                .setControllerListener(controllerListener)
                .build();
    }

    /**
     * 实现两个功能：
     * 1、是否支持在XML中设置图片宽和高中的某一个为WRAP_CONTENT setEnableWrapContent
     * 2、实现图片加载成功和失败的回调 IImageLoadListener
     */
    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
            if (imageInfo == null) {
                if (loadListener == null) {
                    return;
                }
                loadListener.onSuccess(id, 0, 0);
                return;
            }
            if (enableWrapContent) {
                setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
            }
            if (loadListener == null) {
                return;
            }
            loadListener.onSuccess(id, imageInfo.getWidth(), imageInfo.getHeight());
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            FLog.d("FrescoImageView", "Intermediate image received");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            FLog.e(getClass(), throwable, "Error loading %s", id);
            if (loadListener == null) {
                return;
            }
            loadListener.onFailure(id, throwable);
        }
    };

    /**
     * 根据 postprocessor 的性质，目标图片不会永远是完全不透明的。
     * 由于 Bitmap.hasAlpha 方法的返回值，这有时会导致问题。
     * 也就是说如果该方法返回 false(默认值)，Android 会选择不进行混合地快速绘制。
     * 这会导致出现一张用黑色代替透明像素的半透明图片。
     * 为了解决这一问题，将目标图片中该值设为 true。
     */
    private Postprocessor postprocessor = new BasePostprocessor() {
        @Override
        public void process(Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            bitmap.setHasAlpha(true);
        }
    };

}
