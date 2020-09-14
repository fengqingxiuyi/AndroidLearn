package com.example.learn.java.src.structure.pattern_decorator.shape.impl;

import com.example.learn.java.src.structure.pattern_decorator.shape.IShape;

/**
 * @author ShenBF
 * @desc 实现接口的实体类
 * @date 2018/7/24
 */
public class Rectangle implements IShape {

    @Override
    public void draw() {
        System.out.println("Shape: Rectangle");
    }

}
