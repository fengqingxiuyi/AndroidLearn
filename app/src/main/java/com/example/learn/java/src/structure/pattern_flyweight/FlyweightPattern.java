package com.example.learn.java.src.structure.pattern_flyweight;

import com.example.learn.java.src.structure.pattern_flyweight.shape.factory.ShapeFactory;
import com.example.learn.java.src.structure.pattern_flyweight.shape.impl.Circle;

/**
 * @author ShenBF
 * @desc 使用该工厂，通过传递颜色信息来获取实体类的对象
 * @date 2018/7/24
 */
public class FlyweightPattern {

    private static final String colors[] =
            {"Red", "Green", "Blue", "White", "Black"};

    public static void main(String[] args) {
        for (int i = 0; i < 20; ++i) {
            Circle circle = (Circle) ShapeFactory.getCircle(getRandomColor());
            circle.setX(getRandomX());
            circle.setY(getRandomY());
            circle.setRadius(100);
            circle.draw();
        }
    }

    private static String getRandomColor() {
        return colors[(int) (Math.random() * colors.length)];
    }

    private static int getRandomX() {
        return (int) (Math.random() * 100);
    }

    private static int getRandomY() {
        return (int) (Math.random() * 100);
    }

}
