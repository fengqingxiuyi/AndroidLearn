package com.example.learn.java.src.creation.pattern_prototype.prototype.impl;

import com.example.learn.java.src.creation.pattern_prototype.prototype.Shape;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/24
 */
public class Square extends Shape {

    public Square() {
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }

}