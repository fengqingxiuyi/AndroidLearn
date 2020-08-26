package com.example.learn.ui.partition.utils;

import android.content.Context;

import com.example.learn.ui.partition.partition.one.OneBean;
import com.example.learn.ui.partition.partition.two.TwoBean;
import com.example.partition.IPartitionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 */
public class Util {

    /**
     * 将dip或dp值转换为px值，保证尺寸不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 创建假数据
     */
    public static List<IPartitionBean> getData() {
        List<IPartitionBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                // add one
                OneBean oneBean = new OneBean();
                List<OneBean.DataBean> dataBeanList = new ArrayList<>();
                OneBean.DataBean dataBean = new OneBean.DataBean();
                dataBean.setText("分区1数据" + i + "-1");
                dataBeanList.add(dataBean);
                oneBean.setDataBean(dataBeanList);
                oneBean.setLineHeight(10);
                list.add(oneBean);
            } else {
                // add two
                TwoBean twoBean = new TwoBean();
                List<TwoBean.DataBean> dataBeanList = new ArrayList<>();
                TwoBean.DataBean dataBean1 = new TwoBean.DataBean();
                dataBean1.setText("分区2数据" + i + "-1");
                dataBeanList.add(dataBean1);
                TwoBean.DataBean dataBean2 = new TwoBean.DataBean();
                dataBean2.setText("分区2数据" + i + "-2");
                dataBeanList.add(dataBean2);
                twoBean.setDataBean(dataBeanList);
                twoBean.setLineHeight(20);
                list.add(twoBean);
            }
        }
        return list;
    }

}
