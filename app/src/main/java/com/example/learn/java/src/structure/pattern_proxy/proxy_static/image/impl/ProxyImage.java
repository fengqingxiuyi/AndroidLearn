package com.example.learn.java.src.structure.pattern_proxy.proxy_static.image.impl;

import com.example.learn.java.src.structure.pattern_proxy.proxy_static.image.IImage;

/**
 * @author fqxyi
 * @desc 实现接口的实体类
 * @date 2018/7/24
 */
public class ProxyImage implements IImage {

    private IImage target;

    public ProxyImage(IImage target) {
        this.target = target;
    }

    @Override
    public void display() {
        System.out.println("开始事务...");
        target.display();
        System.out.println("提交事务...");
    }

}