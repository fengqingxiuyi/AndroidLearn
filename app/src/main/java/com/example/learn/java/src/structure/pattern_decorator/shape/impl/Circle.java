package com.example.learn.java.src.structure.pattern_decorator.shape.impl;

import com.example.learn.java.src.structure.pattern_decorator.shape.IShape;

/**
 * @author fqxyi
 * @desc 实现接口的实体类
 * @date 2018/7/24
 */
public class Circle implements IShape {

    @Override
    public void draw() {
        System.out.println("Shape: Circle");
    }

}
