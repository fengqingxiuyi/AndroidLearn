package com.example.learn.ui.partition.partition.one;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.ui.partition.IPartitionCallback;
import com.example.learn.ui.partition.partition.line.LineViewHolder;
import com.example.partition.BasePartition;
import com.example.partition.ISpanSize;
import com.example.partition.ItemViewTypeBean;

/**
 * 分区类型1
 */
public class OnePartition extends BasePartition<OneBean> {

    private IPartitionCallback callback;

    public OnePartition(Context context, IPartitionCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public int getSpanSize(int position) {
        if (position == itemList.size()-1) {
            return ISpanSize.ONE;
        }
        return ISpanSize.ONE;
    }

    @Override
    public ItemViewTypeBean getItemViewTypeBean(int position) {
        ItemViewTypeBean bean = new ItemViewTypeBean();
        if (position == itemList.size()-1) {
            bean.itemViewType = 0;
            bean.repeat = false;
        } else {
            bean.itemViewType = getPartitionType();
            bean.repeat = true;
        }
        return bean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            return new LineViewHolder(new View(context));
        }
        return new OneViewHolder(new OneView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof OneViewHolder) {
            OneViewHolder oneViewHolder = (OneViewHolder) viewHolder;
            if (getData() != null && getData().getDataBean() != null) {
                oneViewHolder.oneView.initData(getData().getDataBean().get(position), callback, getPosition());
            }
        }
        if (viewHolder instanceof LineViewHolder) {
            LineViewHolder lineViewHolder = (LineViewHolder) viewHolder;
            if (getData() != null) {
                lineViewHolder.initData(getData().getLineHeight(), Color.CYAN);
            }
        }
    }

    @Override
    public void getItemSize() {
        itemList.add(getPosition());
        itemList.add(getPosition());
    }
}
