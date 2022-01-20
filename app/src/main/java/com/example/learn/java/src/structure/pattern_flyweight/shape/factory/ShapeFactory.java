package com.example.learn.java.src.structure.pattern_flyweight.shape.factory;

import com.example.learn.java.src.structure.pattern_flyweight.shape.IShape;
import com.example.learn.java.src.structure.pattern_flyweight.shape.impl.Circle;

import java.util.HashMap;

/**
 * @author fqxyi
 * @desc 创建一个工厂，生成基于给定信息的实体类的对象
 * @date 2018/7/24
 */
public class ShapeFactory {

    private static final HashMap<String, IShape> circleMap = new HashMap<>();

    public static IShape getCircle(String color) {
        Circle circle = (Circle) circleMap.get(color);

        if (circle == null) {
            circle = new Circle(color);
            circleMap.put(color, circle);
            System.out.println("Creating circle of color : " + color);
        }
        return circle;
    }

}
