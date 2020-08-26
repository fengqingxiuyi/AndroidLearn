package com.example.partition;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 分区基类
 */
public abstract class BasePartition<T extends IPartitionBean> {

    //上下文
    protected Context context;
    //分区数据源
    private T data;
    //同一分区内viewHolder的个数
    protected List<Integer> itemList;
    //分区开始位置
    private int startCount;
    //分区类型
    private int partitionType;
    //分区在partitionList中的位置
    private int position;

    public BasePartition(Context context) {
        this.context = context;
    }

    /**
     * 为区块设置数据源
     * @param data 值可能为null
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 获取区块数据源
     */
    public T getData() {
        return data;
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    List<Integer> getItemList() {
        return itemList;
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    void setItemList() {
        if (itemList == null) {
            itemList = new ArrayList<>();
        } else {
            itemList.clear();
        }
        getItemSize();
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    int getStartCount() {
        return startCount;
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    void setStartCount(int startCount) {
        this.startCount = startCount;
    }

    public int getPartitionType() {
        return partitionType;
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    void setPartitionType(int partitionType) {
        this.partitionType = partitionType;
    }

    public int getPosition() {
        return position;
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    void setPosition(int position) {
        this.position = position;
    }

    public abstract int getSpanSize(int position);
    public abstract ItemViewTypeBean getItemViewTypeBean(int position);
    public abstract RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType);
    public abstract void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position);
    public abstract void getItemSize();

}
