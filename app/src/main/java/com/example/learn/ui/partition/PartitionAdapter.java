package com.example.learn.ui.partition;

import android.content.Context;

import com.example.learn.ui.partition.partition.PartitionFactory;
import com.example.partition.BasePartition;
import com.example.partition.BasePartitionAdapter;
import com.example.partition.IPartitionBean;

public class PartitionAdapter extends BasePartitionAdapter {

    //上下文
    private Context context;
    //回调
    private IPartitionCallback callback;

    public PartitionAdapter(Context context, IPartitionCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public BasePartition createPartition(IPartitionBean bean) {
        return PartitionFactory.createPartition(context, bean, callback);
    }
}
