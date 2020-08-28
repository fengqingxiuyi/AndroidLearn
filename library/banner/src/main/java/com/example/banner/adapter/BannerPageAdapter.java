package com.example.banner.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.banner.holder.Holder;
import com.example.banner.view.BannerViewPager;

import java.util.List;

public class BannerPageAdapter<T> extends PagerAdapter {

    private BannerViewPager<T> viewPager;

    private Holder<T> holderCreator;

    protected List<T> data;

    private boolean canLoop = true;  // 是否支持无限循环

    private int childCount = 0;

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    @Override
    public int getCount() {
        int realCount = getRealCount();
        if (0 == realCount) return 0;
        if (canLoop) return Integer.MAX_VALUE;
        return realCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public int getRealCount() {
        return data == null ? 0 : data.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = getRealPosition(position);
        View view = holderCreator.createView(container.getContext());
        container.addView(view);
        //
        if (data != null && !data.isEmpty() && realPosition < data.size()) {
            holderCreator.updateUI(container.getContext(), view, realPosition, data.get(realPosition));
        }
        return view;
    }

    public int getRealPosition(int position) {
        int realCount = getRealCount();
        if (0 == realCount) return 0;
        if (canLoop) return position % realCount;
        return position;
    }

    public BannerPageAdapter(Holder<T> holderCreator, List<T> data) {
        this.holderCreator = holderCreator;
        this.data = data;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFirstItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void setViewPager(BannerViewPager<T> viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount > 0) {
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        childCount = getCount();
        super.notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

}
