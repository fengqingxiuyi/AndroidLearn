package com.example.learn.java.src.structure.pattern_decorator;

import com.example.learn.java.src.structure.pattern_decorator.shape.IShape;
import com.example.learn.java.src.structure.pattern_decorator.shape.decorator.RedShapeDecorator;
import com.example.learn.java.src.structure.pattern_decorator.shape.impl.Circle;
import com.example.learn.java.src.structure.pattern_decorator.shape.impl.Rectangle;

/**
 * @author fqxyi
 * @desc 使用 RedShapeDecorator 来装饰 Shape 对象 新增行为
 * @date 2018/7/24
 */
public class DecoratorPattern {

    public static void main(String[] args) {
        IShape circle = new Circle();

        IShape redCircle = new RedShapeDecorator(new Circle());

        IShape redRectangle = new RedShapeDecorator(new Rectangle());

        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle of red border");
        redCircle.draw();

        System.out.println("\nRectangle of red border");
        redRectangle.draw();
    }

}
