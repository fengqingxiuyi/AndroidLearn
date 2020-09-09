package com.example.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.LayoutRes;

import java.util.List;

/**
 * @author fqxyi
 * @date 2017/5/19
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> data;

    public CommonAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
    }

    public abstract @LayoutRes
    int getLayoutId();

    /**
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public T getItem(int position) {
        if (null == data || data.size() <= position) {
            return null;
        }
        return data.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent, getLayoutId(), position);

        convert(holder, getItem(position), position);

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    public void setData(List<T> data) {
        this.data = data;
    }
}
