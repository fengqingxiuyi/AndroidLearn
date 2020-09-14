package com.example.learn.java.src.creation.pattern_factory_abstract.shape.impl;

import com.example.learn.java.src.creation.pattern_factory_abstract.shape.IShape;

/**
 * @author ShenBF
 * @desc 圆形，具体实现类
 * @date 2018/7/24
 */
public class Circle implements IShape {

    @Override
    public void draw() {
        System.out.println("画了一个圆");
    }

}
