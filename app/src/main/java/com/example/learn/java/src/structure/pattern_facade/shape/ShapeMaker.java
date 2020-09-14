package com.example.learn.java.src.structure.pattern_facade.shape;

import com.example.learn.java.src.structure.pattern_facade.shape.impl.Circle;
import com.example.learn.java.src.structure.pattern_facade.shape.impl.Rectangle;
import com.example.learn.java.src.structure.pattern_facade.shape.impl.Square;

/**
 * @author ShenBF
 * @desc 外观类
 * @date 2018/7/24
 */
public class ShapeMaker {

    private IShape circle;
    private IShape rectangle;
    private IShape square;

    public ShapeMaker() {
        circle = new Circle();
        rectangle = new Rectangle();
        square = new Square();
    }

    public void drawCircle() {
        circle.draw();
    }

    public void drawRectangle() {
        rectangle.draw();
    }

    public void drawSquare() {
        square.draw();
    }

}
