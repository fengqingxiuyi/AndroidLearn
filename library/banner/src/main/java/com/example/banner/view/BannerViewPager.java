package com.example.banner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.example.banner.adapter.BannerPageAdapter;
import com.example.banner.listener.BannerItemClickListener;

public class BannerViewPager<T> extends ViewPager {

    private BannerPageAdapter<T> adapter;
    private OnPageChangeListener listener;

    private boolean isCanScroll = true; // 能否滑动视图
    private boolean canLoop = true; // 是否支持无限循环

    private BannerItemClickListener bannerItemClickListener;

    public BannerViewPager(Context context) {
        super(context);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnPageChangeListener(onPageChangeListener);
    }

    public void setPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private float oldX = 0;
    private static final float sens = 5;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            if (bannerItemClickListener != null) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = ev.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float newX = ev.getX();
                        if (Math.abs(oldX - newX) < sens) {
                            bannerItemClickListener.onItemClick((getRealItem()));
                        }
                        oldX = 0;
                        break;
                }
            }
            try {
                return super.onTouchEvent(ev);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setBannerItemClickListener(BannerItemClickListener bannerItemClickListener) {
        this.bannerItemClickListener = bannerItemClickListener;
    }

    public void setAdapter(BannerPageAdapter<T> adapter, boolean canLoop) {
        this.adapter = adapter;
        this.adapter.setCanLoop(canLoop);
        this.adapter.setViewPager(this);
        super.setAdapter(this.adapter);

        setCurrentItem(getFirstItem(), false);
    }

    public BannerPageAdapter<T> getAdapter() {
        return adapter;
    }

    public int getFirstItem() {
        return canLoop ? adapter.getRealCount() : 0;
    }

    public int getLastItem() {
        return adapter.getRealCount() - 1;
    }

    public int getRealItem() {
        return adapter != null ? adapter.getRealPosition(super.getCurrentItem()) : 0;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        if (!canLoop) {
            setCurrentItem(getRealItem(), false);
        }
        if (adapter == null)
            return;
        adapter.setCanLoop(canLoop);
        adapter.notifyDataSetChanged();
    }

    public boolean isCanLoop() {
        return canLoop;
    }

    public void destroy() {
        if (onPageChangeListener != null) {
            removeOnPageChangeListener(onPageChangeListener);
            onPageChangeListener = null;
        }
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float previousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = adapter.getRealPosition(position);
            if (previousPosition != realPosition) {
                previousPosition = realPosition;
                if (listener != null) {
                    listener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (listener != null) {
                if (position != adapter.getRealCount() - 1) {
                    listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        listener.onPageScrolled(0, 0, 0);
                    } else {
                        listener.onPageScrolled(position, 0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (listener != null) {
                listener.onPageScrollStateChanged(state);
            }
        }
    };

}
