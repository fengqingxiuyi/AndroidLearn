package com.example.learn.java.src.creation.pattern_factory_abstract.shape.factory;

import com.example.learn.java.src.creation.pattern_factory_abstract.color.IColor;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.AbstractFactory;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.IShape;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.impl.Circle;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.impl.Rectangle;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.impl.Square;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.type.ShapeType;

/**
 * @author fqxyi
 * @desc 工厂，生成基于给定信息的实体类的对象
 * @date 2018/7/24
 */
public class ShapeFactory extends AbstractFactory {

    @Override
    public IColor getColor(String color) {
        return null;
    }

    /**
     * 获取形状类型的对象
     * @param shapeType 形状类型
     * @return 形状类型的对象
     */
    @Override
    public IShape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        switch (shapeType) {
            case ShapeType.CIRCLE:
                return new Circle();
            case ShapeType.SQUARE:
                return new Square();
            case ShapeType.RECTANGLE:
                return new Rectangle();
        }
        return null;
    }

}
