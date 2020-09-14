package com.example.learn.java.src.creation.pattern_prototype.prototype.impl;

import com.example.learn.java.src.creation.pattern_prototype.prototype.Shape;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/24
 */
public class Rectangle extends Shape {

    public Rectangle() {
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }

}