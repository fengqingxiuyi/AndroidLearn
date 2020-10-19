package com.example.image.fresco

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import com.example.image.listener.IImageLoadListener
import com.example.image.listener.IImageView
import com.facebook.common.logging.FLog
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.BasePostprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.request.Postprocessor
import okhttp3.OkHttpClient

/**
 * @author fqxyi
 * @date 2018/1/27
 * FrescoImageView
 * 注意点:
 * 1、设置图片宽高，受actualImageScaleType属性值的限制，详见[ScalingUtils]
 * 参考资料：
 * 1、https://www.fresco-cn.org/docs/getting-started.html
 * 2、http://blog.csdn.net/chwnpp2/article/details/51063492
 */
open class FrescoImageView : SimpleDraweeView, IImageView {
    
    protected lateinit var mHierarchy: GenericDraweeHierarchy
    protected var mLoadListener: IImageLoadListener? = null
    protected var mEnableWrapContent = false
    protected var mImageUrl: String? = null
    protected var mImageRes = 0

    constructor(context: Context, hierarchy: GenericDraweeHierarchy) : super(context, hierarchy) {
        init()
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        mHierarchy = hierarchy
        if (mHierarchy == null) {
            val builder = GenericDraweeHierarchyBuilder(resources)
            mHierarchy = builder.build()
            hierarchy = mHierarchy
        }
    }
    /************ 图片属性  */
    /**
     * 设置为圆圈图
     */
    override fun setImageCircle() {
        mHierarchy.roundingParams = RoundingParams.asCircle()
    }

    /**
     * 设置为圆角图
     * @param radius 圆角度数 单位：像素
     */
    override fun setImageRound(radius: Float) {
        mHierarchy.roundingParams = RoundingParams.fromCornersRadius(radius)
    }

    /**
     * 设置图片自适应宽高
     * 适用场景：宽和高 都可以是 WRAP_CONTENT，但是不能为 0
     *
     * @param enableWrapContent 是否自适应
     */
    override fun setEnableWrapContent(enableWrapContent: Boolean) {
        this.mEnableWrapContent = enableWrapContent
    }
    /************ 占位图  */
    /**
     * 设置占位图
     * @param resource 图片资源ID
     */
    override fun setPlaceholderImage(resource: Int) {
        mHierarchy.setPlaceholderImage(resource)
    }

    /**
     * 设置占位图
     * @param resource 图片资源ID
     * @param scaleType 图片缩放类型
     */
    override fun setPlaceholderImage(resource: Int, scaleType: ScalingUtils.ScaleType?) {
        mHierarchy.setPlaceholderImage(resource, scaleType)
    }

    /**
     * 设置占位图
     * @param drawable 图片资源
     */
    override fun setPlaceholderImage(drawable: Drawable?) {
        mHierarchy.setPlaceholderImage(drawable)
    }

    /**
     * 设置占位图
     * @param drawable 图片资源
     * @param scaleType 图片缩放类型
     */
    override fun setPlaceholderImage(drawable: Drawable?, scaleType: ScalingUtils.ScaleType?) {
        mHierarchy.setPlaceholderImage(drawable, scaleType)
    }
    /************ 图片地址  */
    /**
     * 设置本地图片
     * @param resId 本地图片资源ID
     */
    override fun setImageUrl(resId: Int) {
        setImageUrl(resId, null)
    }

    /**
     * 设置本地图片
     * @param resId 本地图片资源ID
     * @param loadListener 图片加载监听器
     */
    override fun setImageUrl(resId: Int, loadListener: IImageLoadListener?) {
        this.mLoadListener = loadListener
        setImageUrl(resId, 0, 0)
    }

    /**
     * 设置本地图片
     * @param resId 本地图片资源ID
     * @param width 图片宽度 单位：像素
     * @param height 图片高度 单位：像素
     */
    override fun setImageUrl(resId: Int, width: Int, height: Int) {
        mImageRes = resId
        setImageUrl("res://" + context.packageName + "/" + resId, width, height)
    }

    /**
     * 设置网络图片
     * @param url 网络图片地址
     */
    override fun setImageUrl(url: String?) {
        setImageUrl(url, null)
    }

    /**
     * 设置网络图片
     * @param url 网络图片地址
     * @param loadListener 图片加载监听器
     */
    override fun setImageUrl(url: String?, loadListener: IImageLoadListener?) {
        this.mLoadListener = loadListener
        val params = layoutParams
        if (null != params) {
            val width = params.width
            val height = params.height
            if (width > 0 && height > 0) {
                setImageUrl(url, width, height)
                return
            }
        }
        setImageUrl(url, 0, 0)
    }

    /**
     * 设置网络图片
     * @param url 网络图片地址
     */
    override fun setImageUrl(url: String?, width: Int, height: Int) {
        var mUrl = url ?: ""
        if (mUrl.startsWith("//")) {
            mUrl = "http:$mUrl"
        }
        mImageUrl = mUrl
        controller = getDraweeController(mUrl, width, height)
    }

    /************ Other  */
    override fun getImageUrl(): String? {
        return mImageUrl
    }

    override fun getImageRes(): Int {
        return mImageRes
    }

    /**
     * 图片加载 最终实现类
     */
    private fun getDraweeController(url: String, width: Int, height: Int): DraweeController {
        val uri = Uri.parse(url)
        val requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
            .setPostprocessor(postprocessor)
        if (width > 0 && height > 0) {
            /*
             * Resizing has some limitations:
             *      it only supports JPEG files
             *      the actual resize is carried out to the nearest 1/8 of the original size
             *      it cannot make your image bigger, only smaller (not a real limitation though)
             */
            requestBuilder.resizeOptions = ResizeOptions(width, height)
        }
        val builder = Fresco.newDraweeControllerBuilder()
        if (url.endsWith(".gif")) {
            builder.autoPlayAnimations = true
        }
        return builder.setImageRequest(requestBuilder.build())
            .setOldController(controller) // ResizeOptions需要
            .setControllerListener(controllerListener)
            .build()
    }

    /**
     * 实现两个功能：
     * 1、是否支持在XML中设置图片宽和高中的某一个为WRAP_CONTENT setEnableWrapContent
     * 2、实现图片加载成功和失败的回调 IImageLoadListener
     */
    private var controllerListener: ControllerListener<ImageInfo> = object : BaseControllerListener<ImageInfo>() {
        override fun onFinalImageSet(id: String, imageInfo: ImageInfo?, anim: Animatable?) {
            if (imageInfo == null) {
                mLoadListener?.onSuccess(id, 0, 0)
                return
            }
            if (mEnableWrapContent) {
                aspectRatio = imageInfo.width.toFloat() / imageInfo.height
            }
            mLoadListener?.onSuccess(id, imageInfo.width, imageInfo.height)
        }

        override fun onIntermediateImageSet(id: String, imageInfo: ImageInfo?) {
            FLog.d("FrescoImageView", "Intermediate image received")
        }

        override fun onFailure(id: String, throwable: Throwable) {
            FLog.e(javaClass, throwable, "Error loading %s", id)
            mLoadListener?.onFailure(id, throwable)
        }
    }

    /**
     * 根据 postprocessor 的性质，目标图片不会永远是完全不透明的。
     * 由于 Bitmap.hasAlpha 方法的返回值，这有时会导致问题。
     * 也就是说如果该方法返回 false(默认值)，Android 会选择不进行混合地快速绘制。
     * 这会导致出现一张用黑色代替透明像素的半透明图片。
     * 为了解决这一问题，将目标图片中该值设为 true。
     */
    private val postprocessor: Postprocessor = object : BasePostprocessor() {
        override fun process(bitmap: Bitmap?) {
            bitmap?.setHasAlpha(true)
        }
    }

    companion object {
        /**
         * Fresco 初始化
         *
         * 注意点：
         * 1、setDownsampleEnabled：必须和ImageRequest的ResizeOptions一起使用，
         * 作用就是在图片解码时根据ResizeOptions所设的宽高的像素进行解码，这样解码出来可以得到一个更小的Bitmap。
         * ResizeOptions和DownsampleEnabled参数都不影响原图片的大小，影响的是EncodeImage的大小，进而影响Decode出来的Bitmap的大小，
         * ResizeOptions须和此参数结合使用是因为单独使用ResizeOptions的话只支持JPEG图，所以需支持png、jpg、webp需要先设置此参数。
         */
        @JvmOverloads
        fun init(context: Context, okHttpClient: OkHttpClient? = null) {
            if (okHttpClient == null) {
                val config = ImagePipelineConfig
                    .newBuilder(context)
                    .setDownsampleEnabled(true)
                    .build()
                Fresco.initialize(context, config)
                return
            }
            val config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, okHttpClient)
                .setDownsampleEnabled(true)
                .build()
            Fresco.initialize(context, config)
        }
        /************ 缓存  */
        /**
         * 清除内存缓存
         */
        fun clearMemoryCaches() {
            Fresco.getImagePipeline().clearMemoryCaches()
        }

        /**
         * 清除磁盘缓存
         */
        fun clearDiskCaches() {
            Fresco.getImagePipeline().clearDiskCaches()
        }

        /**
         * 清除所有缓存
         */
        fun clearCaches() {
            clearMemoryCaches()
            clearDiskCaches()
        }
    }
}