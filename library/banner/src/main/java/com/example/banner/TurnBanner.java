package com.example.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.viewpager.widget.ViewPager;

import com.example.banner.adapter.BannerPageAdapter;
import com.example.banner.holder.Holder;
import com.example.banner.listener.BannerItemClickListener;
import com.example.banner.listener.CustomPageChangeListener;
import com.example.banner.listener.PointChangeListener;
import com.example.banner.view.BannerViewPager;
import com.example.banner.view.ViewPagerScroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面翻转控件
 */
public class TurnBanner<T> extends LinearLayout {

    // layout
    private BannerViewPager<T> bannerViewPager;
    private ViewGroup pointerContainer;
    // attr
    private boolean turning; //是否正在自动翻页
    private long autoTurnTime; //自动翻页时间间隔
    private boolean canTurn = false; //能否手动触发翻页
    private boolean canLoop = true; //是否支持无限循环
    // component
    private ViewPagerScroller scroller;
    private BannerPageAdapter<T> pageAdapter;
    private CustomPageChangeListener customPageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private SwitchTask<T> switchTask;
    // data
    private List<T> data;
    private List<ImageView> pointViews = new ArrayList<>();

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    public TurnBanner(Context context) {
        this(context, null);
        init(context, null);
    }

    public TurnBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TurnBanner);
            canLoop = typedArray.getBoolean(R.styleable.TurnBanner_canLoop, true);
            autoTurnTime = typedArray.getInteger(R.styleable.TurnBanner_autoTurningTime, 3000);
            typedArray.recycle();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, this, true);
        bannerViewPager = view.findViewById(R.id.banner_view_pager);
        pointerContainer = view.findViewById(R.id.banner_point_container);
        // 初始化ViewPager的滑动速度
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(getContext());
            mScroller.set(bannerViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switchTask = new SwitchTask<T>(this);
    }

    /**
     * 设置ViewPager的滚动速度
     */
    public TurnBanner<T> setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
        return this;
    }

    /**
     * 初始化页面 但 不初始化数据
     */
    public TurnBanner<T> setPages(Holder holderCreator) {
        setPages(holderCreator, null);
        return this;
    }

    /**
     * 初始化页面 并且 初始化数据
     */
    public TurnBanner<T> setPages(Holder holderCreator, List<T> data) {
        this.data = data;
        pageAdapter = new BannerPageAdapter<T>(holderCreator, data);
        bannerViewPager.setAdapter(pageAdapter, canLoop);
        return this;
    }

    /**
     * 仅初始化数据
     */
    public TurnBanner<T> setData(List data) {
        this.data = data;
        if (null == pageAdapter) return this;
        pageAdapter.setData(data);
        return this;
    }

    public int getDataSize() {
        return null == data ? 0 : data.size();
    }

    public List<T> getData() {
        return data;
    }

    /***
     * 开始翻页
     * @param autoTurnTime 翻页时间
     */
    public TurnBanner<T> startAutoTurn(long autoTurnTime) {
        if (autoTurnTime < 1000) {
            autoTurnTime = 1000;
        }
        //如果是正在翻页的话先停掉
        if (turning) {
            stopAutoTurn();
        }
        setCanScroll(true);
        //设置可以翻页并开启翻页
        canTurn = true;
        //正在自动翻页
        turning = true;
        //自动翻页时间间隔
        this.autoTurnTime = autoTurnTime;
        //开始翻页
        postDelayed(switchTask, autoTurnTime);
        return this;
    }

    /**
     * 开启翻页 如果是正在翻页的话先停止翻页，再启动翻页
     */
    public TurnBanner<T> startAutoTurn() {
        startAutoTurn(autoTurnTime);
        return this;
    }

    /**
     * 暂停翻页 手动触摸会重新启动自动翻页
     */
    public TurnBanner<T> pauseAutoTurn() {
        turning = false;
        removeCallbacks(switchTask);
        return this;
    }

    /**
     * 停止翻页 禁止手动触摸
     */
    public TurnBanner<T> stopAutoTurn() {
        canTurn = false;
        setCanScroll(false);
        pauseAutoTurn();
        return this;
    }

    /***
     * 是否正在自动翻页
     */
    public boolean isTurning() {
        return turning;
    }

    /**
     * 设置能否手动触发翻页
     */
    public void setCanTurn(boolean canTurn) {
        this.canTurn = canTurn;
    }

    public boolean isCanTurn() {
        return canTurn;
    }

    /**
     * 设置是否可以循环播放
     */
    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        bannerViewPager.setCanLoop(canLoop);
    }

    public boolean isCanLoop() {
        return bannerViewPager.isCanLoop();
    }

    /**
     * 设置是否可以滚动
     */
    public void setCanScroll(boolean isCanScroll) {
        bannerViewPager.setCanScroll(isCanScroll);
    }

    public boolean isCanScroll() {
        return bannerViewPager.isCanScroll();
    }

    /**
     * 触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_CANCEL ||
                action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startAutoTurn();
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 暂停翻页
            if (canTurn) pauseAutoTurn();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置当前的页面index
     */
    public void setcurrentitem(int index) {
        if (bannerViewPager != null) {
            bannerViewPager.setCurrentItem(index);
        }
    }

    /**
     * 获取当前的页面index
     */
    public int getCurrentItem() {
        if (bannerViewPager != null) {
            return bannerViewPager.getRealItem();
        }
        return -1;
    }

    /**
     * 自定义指示器样式：. . . . .
     *
     * pointImgIds大小只能为2
     */
    public TurnBanner<T> setPageIndicator(int[] pointImgIds, int right, int bottom) {
        if (null == pointImgIds || pointImgIds.length != 2) {
            throw new RuntimeException("pointImgIds大小只能为2");
        }
        if (null == data) return this;
        // clear view
        pointerContainer.removeAllViews();
        // clear data
        pointViews.clear();
        // get view
        for (int count = 0; count < data.size(); count++) {
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(0, 0, right, bottom);
            if (pointViews.isEmpty()) {
                pointView.setImageResource(pointImgIds[1]);
            } else {
                pointView.setImageResource(pointImgIds[0]);
            }
            pointViews.add(pointView);
            pointerContainer.addView(pointView);
        }
        if (null == customPageChangeListener) {
            customPageChangeListener = new CustomPageChangeListener(pointViews, pointImgIds);
        } else {
            customPageChangeListener.setPointImgData(pointViews, pointImgIds);
        }
        bannerViewPager.setPageChangeListener(customPageChangeListener);
        if (null != onPageChangeListener) customPageChangeListener.setPageChangeListener(onPageChangeListener);
        return this;
    }

    /**
     * 完全自定义的指示点布局
     * @param layoutId 外部传入的指示点布局
     */
    public TurnBanner<T> setPageIndicator(@LayoutRes int layoutId, PointChangeListener pointChangeListener) {
        if (null == data || null == pointChangeListener) return this;
        // clear view
        pointerContainer.removeAllViews();
        // clear data
        pointViews.clear();
        // add view
        View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
        pointerContainer.addView(view);
        // other
        if (null == customPageChangeListener) {
            customPageChangeListener = new CustomPageChangeListener(view, data.size(), pointChangeListener);
        } else {
            customPageChangeListener.setPointChangeListener(view, data.size(), pointChangeListener);
        }
        bannerViewPager.setPageChangeListener(customPageChangeListener);
        if (null != onPageChangeListener) customPageChangeListener.setPageChangeListener(onPageChangeListener);
        return this;
    }

    /**
     * 设置底部指示器是否可见
     */
    public TurnBanner<T> setPointViewVisible(boolean visible) {
        pointerContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：
     *              居左 （RelativeLayout.ALIGN_PARENT_LEFT），
     *              居中 （RelativeLayout.CENTER_HORIZONTAL），
     *              居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     */
    public TurnBanner<T> setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pointerContainer.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        pointerContainer.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 自定义翻页动画效果
     */
    public TurnBanner<T> setPageTransformer(ViewPager.PageTransformer transformer) {
        bannerViewPager.setPageTransformer(true, transformer);
        return this;
    }

    public BannerViewPager<T> getBannerViewPager() {
        return bannerViewPager;
    }

    /**
     * 设置翻页监听器
     */
    public TurnBanner<T> setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        // 如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (customPageChangeListener != null) {
            customPageChangeListener.setPageChangeListener(onPageChangeListener);
        } else {
            bannerViewPager.setPageChangeListener(onPageChangeListener);
        }
        return this;
    }

    /**
     * 监听item点击
     */
    public TurnBanner<T> setOnItemClickListener(BannerItemClickListener bannerItemClickListener) {
        bannerViewPager.setBannerItemClickListener(bannerItemClickListener);
        return this;
    }

    public void destroy() {
        bannerViewPager.destroy();
    }

    static class SwitchTask<T> implements Runnable {

        private final WeakReference<TurnBanner<T>> reference;

        SwitchTask(TurnBanner<T> turnBanner) {
            this.reference = new WeakReference<>(turnBanner);
        }

        @Override
        public void run() {
            TurnBanner<T> turnBanner = reference.get();

            if (turnBanner != null) {
                if (turnBanner.bannerViewPager != null && turnBanner.turning) {
                    int page = turnBanner.bannerViewPager.getCurrentItem() + 1;
                    if (page >= turnBanner.pageAdapter.getCount()) page = 0;
                    turnBanner.bannerViewPager.setCurrentItem(page);
                    turnBanner.postDelayed(turnBanner.switchTask, turnBanner.autoTurnTime);
                }
            }
        }
    }

}
