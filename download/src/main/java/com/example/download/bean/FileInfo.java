package com.example.download.bean;

import java.io.Serializable;

/**
 * @author ShenBF
 * @描述:
 * @date 2018/4/20
 */
public class FileInfo implements Serializable {

    public static final String KEY = "FileInfo";

    public int id;
    public String url;
    public String fileName;
    public int length;
    public int finished;

    public FileInfo(int id, String url, String fileName, int length, int finished) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", length=" + length +
                ", finished=" + finished +
                '}';
    }
}
