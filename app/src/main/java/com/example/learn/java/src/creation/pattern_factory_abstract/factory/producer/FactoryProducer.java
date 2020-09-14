package com.example.learn.java.src.creation.pattern_factory_abstract.factory.producer;

import com.example.learn.java.src.creation.pattern_factory_abstract.color.factory.ColorFactory;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.AbstractFactory;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.type.FactoryType;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.factory.ShapeFactory;

/**
 * @author ShenBF
 * @desc 工厂创造器/生成器类
 * @date 2018/7/24
 */
public class FactoryProducer {

    public static AbstractFactory getFactory(String factory) {
        if (factory == null) {
            return null;
        }
        switch (factory) {
            case FactoryType.COLOR:
                return new ColorFactory();
            case FactoryType.SHAPE:
                return new ShapeFactory();
        }
        return null;
    }

}
