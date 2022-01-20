package com.example.learn.java.src.creation.pattern_prototype.prototype.cache;

import com.example.learn.java.src.creation.pattern_prototype.prototype.Shape;
import com.example.learn.java.src.creation.pattern_prototype.prototype.impl.Circle;
import com.example.learn.java.src.creation.pattern_prototype.prototype.impl.Rectangle;
import com.example.learn.java.src.creation.pattern_prototype.prototype.impl.Square;

import java.util.Hashtable;

/**
 * @author fqxyi
 * @desc 创建一个类，从数据库获取实体类，并把它们存储在一个 Hashtable 中。
 * @date 2018/7/24
 */
public class ShapeCache {

    private static Hashtable<String, Shape> shapeMap
            = new Hashtable<String, Shape>();

    public static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return (Shape) cachedShape.clone();
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    public static void loadCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(), circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(), square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(), rectangle);
    }

}
