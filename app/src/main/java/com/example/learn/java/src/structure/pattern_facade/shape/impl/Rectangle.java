package com.example.learn.java.src.structure.pattern_facade.shape.impl;

import com.example.learn.java.src.structure.pattern_facade.shape.IShape;

/**
 * @author fqxyi
 * @desc 接口实现类
 * @date 2018/7/24
 */
public class Rectangle implements IShape {

    @Override
    public void draw() {
        System.out.println("Rectangle::draw()");
    }

}
