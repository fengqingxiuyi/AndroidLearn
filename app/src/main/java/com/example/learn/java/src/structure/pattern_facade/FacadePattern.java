package com.example.learn.java.src.structure.pattern_facade;

import com.example.learn.java.src.structure.pattern_facade.shape.ShapeMaker;

/**
 * @author ShenBF
 * @desc 使用该外观类画出各种类型的形状
 * @date 2018/7/24
 */
public class FacadePattern {

    public static void main(String[] args) {
        ShapeMaker shapeMaker = new ShapeMaker();

        shapeMaker.drawCircle();
        shapeMaker.drawRectangle();
        shapeMaker.drawSquare();
    }

}
