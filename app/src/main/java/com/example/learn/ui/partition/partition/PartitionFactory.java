package com.example.learn.ui.partition.partition;

import android.content.Context;

import com.example.learn.ui.partition.IPartitionCallback;
import com.example.learn.ui.partition.partition.one.OnePartition;
import com.example.learn.ui.partition.partition.two.TwoPartition;
import com.example.partition.BasePartition;
import com.example.partition.IPartitionBean;

/**
 * 区块创建类
 */
public class PartitionFactory {

    /**
     * 创建分区
     */
    public static BasePartition createPartition(Context context, IPartitionBean bean, IPartitionCallback callback) {
        if (bean == null) {
            return null;
        }
        switch (bean.getPartitionType()) {
            case 1:
                return new OnePartition(context, callback);
            case 2:
                return new TwoPartition(context, callback);
            default:
                return null;
        }
    }

}
