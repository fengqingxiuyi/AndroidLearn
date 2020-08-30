package com.example.ui.loop;

import android.view.View;

import java.util.List;

/**
 * ItemView各种操作实现均由业务类实现
 */
public interface IItemView {
    // 业务类 自行实现 ItemView
    View getItemView();
    // 数据集合 RollLoopView不关心数据类型，只关心数据大小
    List getDataList();
    // 执行具体的业务操作
    void perform(View view, int position);
}
