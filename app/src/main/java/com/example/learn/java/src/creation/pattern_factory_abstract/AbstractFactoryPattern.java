package com.example.learn.java.src.creation.pattern_factory_abstract;

import com.example.learn.java.src.creation.pattern_factory_abstract.color.IColor;
import com.example.learn.java.src.creation.pattern_factory_abstract.color.type.ColorType;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.AbstractFactory;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.producer.FactoryProducer;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.type.FactoryType;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.IShape;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.type.ShapeType;

/**
 * @author ShenBF
 * @desc 抽象工厂模式
 * @date 2018/7/24
 */
public class AbstractFactoryPattern {

    public static void main(String[] args) {
        dealColorFactory();
        dealShapeFactory();
    }

    private static void dealColorFactory() {
        //获取颜色工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory(FactoryType.COLOR);
        if (colorFactory == null) {
            return;
        }
        //获取 Red 的对象，并调用它的 fill 方法
        IColor redColor = colorFactory.getColor(ColorType.RED);
        if (redColor != null) {
            redColor.fill();
        }
        //获取 Green 的对象，并调用它的 fill 方法
        IColor greenColor = colorFactory.getColor(ColorType.GREEN);
        if (greenColor != null) {
            greenColor.fill();
        }
        //获取 Blue 的对象，并调用它的 fill 方法
        IColor blueColor = colorFactory.getColor(ColorType.BLUE);
        if (blueColor != null) {
            blueColor.fill();
        }
    }

    private static void dealShapeFactory() {
        //获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory(FactoryType.SHAPE);
        if (shapeFactory == null) {
            return;
        }
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
