package com.example.learn.ui.partition.partition.two;

import com.example.partition.IPartitionBean;

import java.util.List;

/**
 * 一行两个item的区块的数据结构
 */
public class TwoBean implements IPartitionBean {

    private int lineHeight;
    private List<DataBean> dataBean;

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public List<DataBean> getDataBean() {
        return dataBean;
    }

    public void setDataBean(List<DataBean> dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean {

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @Override
    public int getPartitionType() {
        return 2;
    }

}
