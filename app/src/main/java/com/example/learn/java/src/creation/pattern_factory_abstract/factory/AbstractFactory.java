package com.example.learn.java.src.creation.pattern_factory_abstract.factory;

import com.example.learn.java.src.creation.pattern_factory_abstract.color.IColor;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.IShape;

/**
 * @author fqxyi
 * @desc 工厂抽象类
 * @date 2018/7/24
 */
public abstract class AbstractFactory {

    public abstract IColor getColor(String color);
    public abstract IShape getShape(String shape) ;

}
