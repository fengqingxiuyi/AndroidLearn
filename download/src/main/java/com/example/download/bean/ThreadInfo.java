package com.example.download.bean;

import java.io.Serializable;

/**
 * @author ShenBF
 * @描述:
 * @date 2018/4/20
 */
public class ThreadInfo implements Serializable {

    public static final String KEY_FINISHED = "finished";

    public int id;       //线程ID
    public String url;   //下载地址
    public int start;    //下载开始位置
    public int end;      //下载结束位置
    public int finished; //完成进度

    public ThreadInfo(int id, String url, int start, int end, int finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finished=" + finished +
                '}';
    }
}
