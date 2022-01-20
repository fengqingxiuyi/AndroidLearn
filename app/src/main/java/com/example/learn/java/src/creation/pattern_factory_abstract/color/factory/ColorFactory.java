package com.example.learn.java.src.creation.pattern_factory_abstract.color.factory;

import com.example.learn.java.src.creation.pattern_factory_abstract.color.IColor;
import com.example.learn.java.src.creation.pattern_factory_abstract.color.impl.Blue;
import com.example.learn.java.src.creation.pattern_factory_abstract.color.impl.Green;
import com.example.learn.java.src.creation.pattern_factory_abstract.color.impl.Red;
import com.example.learn.java.src.creation.pattern_factory_abstract.color.type.ColorType;
import com.example.learn.java.src.creation.pattern_factory_abstract.factory.AbstractFactory;
import com.example.learn.java.src.creation.pattern_factory_abstract.shape.IShape;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/24
 */
public class ColorFactory extends AbstractFactory {

    /**
     * 获取颜色类型的对象
     * @param colorType 颜色类型
     * @return 颜色类型的对象
     */
    @Override
    public IColor getColor(String colorType) {
        if (colorType == null) {
            return null;
        }
        switch (colorType) {
            case ColorType.RED:
                return new Red();
            case ColorType.GREEN:
                return new Green();
            case ColorType.BLUE:
                return new Blue();
        }
        return null;
    }

    @Override
    public IShape getShape(String shape) {
        return null;
    }

}
