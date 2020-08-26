package com.example.partition;

/**
 * 由业务类决定创建的分区是否可复用
 */
public class ItemViewTypeBean {

    //分区类型
    public int itemViewType;
    //当前itemViewType是否可重用 true 可重用
    public boolean repeat;

}
