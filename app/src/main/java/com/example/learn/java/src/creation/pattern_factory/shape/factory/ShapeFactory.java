package com.example.learn.java.src.creation.pattern_factory.shape.factory;

import com.example.learn.java.src.creation.pattern_factory.shape.IShape;
import com.example.learn.java.src.creation.pattern_factory.shape.impl.Circle;
import com.example.learn.java.src.creation.pattern_factory.shape.impl.Rectangle;
import com.example.learn.java.src.creation.pattern_factory.shape.impl.Square;
import com.example.learn.java.src.creation.pattern_factory.shape.type.ShapeType;

/**
 * @author ShenBF
 * @desc 工厂，生成基于给定信息的实体类的对象
 * @date 2018/7/24
 */
public class ShapeFactory {

    /**
     * 获取形状类型的对象
     * @param shapeType 形状类型
     * @return 形状类型的对象
     */
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
