package com.example.learn.java.src.structure.pattern_bridge.shape.impl;

import com.example.learn.java.src.structure.pattern_bridge.draw.DrawAPI;
import com.example.learn.java.src.structure.pattern_bridge.shape.Shape;

/**
 * @author fqxyi
 * @desc 实现了 Shape 接口的实体类
 * @date 2018/7/24
 */
public class Circle extends Shape {

    private int x, y, radius;

    public Circle(int x, int y, int radius, DrawAPI drawAPI) {
        super(drawAPI);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void draw() {
        drawAPI.drawCircle(radius, x, y);
    }

}