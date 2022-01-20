package com.example.learn.java.src.creation.pattern_prototype.prototype.impl;

import com.example.learn.java.src.creation.pattern_prototype.prototype.Shape;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/24
 */
public class Circle extends Shape {

    public Circle() {
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }

}