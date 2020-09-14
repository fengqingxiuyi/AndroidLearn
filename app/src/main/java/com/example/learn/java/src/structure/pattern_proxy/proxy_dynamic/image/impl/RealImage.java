package com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic.image.impl;

import com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic.image.IImage;

/**
 * @author ShenBF
 * @desc 实现接口的实体类
 * @date 2018/7/24
 */
public class RealImage implements IImage {

    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        System.out.println("Displaying " + fileName);
    }

}
