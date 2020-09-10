package com.example.ui.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.ui.R;

/**
 * @author fqxyi
 * @date 2018/3/1
 * 最简单的方式实现圆角图片
 * 来源：http://blog.csdn.net/u010072711/article/details/52072721
 */
@Deprecated
public class RoundImageView2 extends View {
    
    private static final String TAG = RoundImageView2.class.getSimpleName();

    private Context mContext;
    /**
     * 默认圆角大小
     */
    private static final int DEFAULT_RADIUS = 10;
    /**
     * 源图片
     */
    private Bitmap mSrc;
    /**
     * 圆角大小，默认为10
     */
    private int mRadius = DEFAULT_RADIUS;
    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;

    public RoundImageView2(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoundImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoundImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public RoundImageView2(Context context, Bitmap bitmap) {
        super(context);
        this.mSrc = bitmap;
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Log.d(TAG, "create RoundImageView");
        mContext = context;
        if (attrs != null) {
            // Load the styled attributes and set their properties
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView2, defStyleAttr, 0);
            mSrc = BitmapFactory.decodeResource(context.getResources(), typedArray.getResourceId(R.styleable.RoundImageView2_src, 0));
            mRadius = (int) typedArray.getDimension(R.styleable.RoundImageView2_radius2, dp2px(DEFAULT_RADIUS));
            typedArray.recycle();
        }

    }

    /**
     * 测量控件大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
         * 三种测量模式解释：
         *  UNSPECIFIED：父布局没有给子布局任何限制，子布局可以任意大小。
         *  EXACTLY：父布局决定子布局的确切大小。不论子布局多大，它都必须限制在这个界限里。match_parent
         *  AT_MOST：此时子控件尺寸只要不超过父控件允许的最大尺寸,子布局可以根据自己的大小选择任意大小。wrap_content
         *
         * 简单的映射关系：
         *  wrap_content -> MeasureSpec.AT_MOST
         *  match_parent -> MeasureSpec.EXACTLY
         *  具体值 -> MeasureSpec.EXACTLY
         */

        // 获取宽高的测量模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        // 获取宽高的尺寸
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        /**
         * 测量宽度
         */
        if (widthSpecMode == MeasureSpec.EXACTLY) {  // 宽为具体值或者是填满父控件就直接赋值 match_parent , accurate
            mWidth = widthSpecSize;
        } else {
            // 图片显示时原始大小
            int srcWidth = mSrc.getWidth() + getPaddingLeft() + getPaddingRight();
            if (widthSpecMode == MeasureSpec.AT_MOST) { // wrap_content,子控件不能超过父控件,此时我们取传递过来的大小和图片本身大小的小者
                mWidth = Math.min(widthSpecSize, srcWidth);
            } else {
                //没有要求，可以随便大小
                mWidth = srcWidth;
            }
        }

        /**
         * 测量高度，逻辑跟测量宽度是一样的
         */
        if (heightSpecMode == MeasureSpec.EXACTLY) {  // match_parent , accurate
            mHeight = heightSpecSize;
        } else {
            // 图片显示时原始大小
            int srcHeight = mSrc.getHeight() + getPaddingTop() + getPaddingBottom();
            if (heightSpecMode == MeasureSpec.AT_MOST) { // wrap_content
                mHeight = Math.min(heightSpecSize, srcHeight);
            } else {
                // 没有要求，可以随便大小
                mHeight = srcHeight;
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 绘制控件
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mSrc == null || mSrc.isRecycled()) {
            return;
        }
        Log.d(TAG, "onDraw");
//        super.onDraw(canvas);
        canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
    }

    /**
     * 设置圆角大小
     */
    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    /**
     * 设置图片
     */
    public void setSrc(Bitmap bitmap) {
        this.mSrc = bitmap;
    }

    /**
     * 设置图片
     */
    public void setSrc(int resId) {
        this.mSrc = BitmapFactory.decodeResource(mContext.getResources(), resId);
    }

    /**
     * 获取图片
     */
    public Bitmap getSrc() {
        return mSrc;
    }

    /**
     * 根据给定的图片和已经测量出来的宽高来绘制圆角图形
     * 原理：
     * 基本原理就是先画一个圆角的图形出来，然后在圆角图形上画我们的源图片，
     * 圆角图形跟我们的源图片堆叠时我们取交集并显示上层的图形
     * 原理就是这样，很简单。
     */
    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint();
        // 开启抗锯齿
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        /**
         * Construct a canvas with the specified bitmap to draw into. The bitmapmust be mutable
         * 以bitmap对象创建一个画布，则将内容都绘制在bitmap上，bitmap不得为null;
         */
        Canvas canvas = new Canvas(target);
        // 新建一个矩形绘制区域,并给出左上角和右下角的坐标
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        // 把图片缩放成我们想要的大小
        source = Bitmap.createScaledBitmap(source, mWidth, mHeight, false);
        // 在绘制矩形区域绘制用画笔绘制一个圆角矩形
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        /**
         * 我简单理解为设置画笔在绘制时图形堆叠时候的显示模式
         * SRC_IN:取两层绘制交集。显示上层。
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
