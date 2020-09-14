package com.example.learn.java.src.creation.pattern_factory;

import com.example.learn.java.src.creation.pattern_factory.shape.IShape;
import com.example.learn.java.src.creation.pattern_factory.shape.factory.ShapeFactory;
import com.example.learn.java.src.creation.pattern_factory.shape.type.ShapeType;

/**
 * @author ShenBF
 * @desc 工厂模式
 * @date 2018/7/24
 */
public class FactoryPattern {

    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();
        //获取 Circle 的对象，并调用它的 draw 方法
        IShape circleShape = shapeFactory.getShape(ShapeType.CIRCLE);
        if (circleShape != null) {
            circleShape.draw();
        }
        //获取 Square 的对象，并调用它的 draw 方法
        IShape squareShape = shapeFactory.getShape(ShapeType.SQUARE);
        if (squareShape != null) {
            squareShape.draw();
        }
        //获取 Rectangle 的对象，并调用它的 draw 方法
        IShape rectangleShape = shapeFactory.getShape(ShapeType.RECTANGLE);
        if (rectangleShape != null) {
            rectangleShape.draw();
        }
    }

}
