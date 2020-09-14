package com.example.learn.java.src.structure.pattern_decorator.shape.decorator;

import com.example.learn.java.src.structure.pattern_decorator.shape.IShape;

/**
 * @author ShenBF
 * @desc 创建扩展了 ShapeDecorator 类的实体装饰类
 * @date 2018/7/24
 */
public class RedShapeDecorator extends ShapeDecorator {

    public RedShapeDecorator(IShape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }

    private void setRedBorder(IShape decoratedShape) {
        System.out.println("Border Color: Red");
    }

}
