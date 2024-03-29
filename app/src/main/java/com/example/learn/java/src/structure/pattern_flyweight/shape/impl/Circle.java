package com.example.learn.java.src.structure.pattern_flyweight.shape.impl;

import com.example.learn.java.src.structure.pattern_flyweight.shape.IShape;

/**
 * @author fqxyi
 * @desc 实现接口的实体类
 * @date 2018/7/24
 */
public class Circle implements IShape {

    private String color;
    private int x;
    private int y;
    private int radius;

    public Circle(String color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Circle: Draw() [Color : " + color
                + ", x : " + x + ", y :" + y + ", radius :" + radius);
    }

}
