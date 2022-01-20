package com.example.learn.java.src.structure.pattern_decorator.shape.decorator;

import com.example.learn.java.src.structure.pattern_decorator.shape.IShape;

/**
 * @author fqxyi
 * @desc 创建实现了 IShape 接口的抽象装饰类
 * @date 2018/7/24
 */
public abstract class ShapeDecorator implements IShape {

    protected IShape decoratedShape;

    public ShapeDecorator(IShape decoratedShape) {
        this.decoratedShape = decoratedShape;
    }

    public void draw() {
        decoratedShape.draw();
    }

}
