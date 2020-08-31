package com.example.learn.ui.card.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.ui.card.adapter.RecyclingPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TubatuAdapter extends RecyclingPagerAdapter {

    private final List<String> mList;
    private final Context mContext;

    public TubatuAdapter(Context context) {
        mList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ItemView itemView;
        if (convertView == null) {
            itemView = new ItemView(mContext);
        } else {
            itemView = (ItemView) convertView;
        }
        itemView.setTag(position);
        itemView.initData(mList.get(position));
        return itemView;
    }

    public void addAll(List<String> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
}