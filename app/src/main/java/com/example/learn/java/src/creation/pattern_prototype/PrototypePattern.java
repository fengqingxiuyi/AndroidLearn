package com.example.learn.java.src.creation.pattern_prototype;

import com.example.learn.java.src.creation.pattern_prototype.prototype.Shape;
import com.example.learn.java.src.creation.pattern_prototype.prototype.cache.ShapeCache;

/**
 * @author fqxyi
 * @desc 原型模式 使用 ShapeCache 类来获取存储在 Hashtable 中的形状的克隆。
 * @date 2018/7/24
 */
public class PrototypePattern {

    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape clonedShape = (Shape) ShapeCache.getShape("1");
        System.out.println("Shape : " + clonedShape.getType());

        Shape clonedShape2 = (Shape) ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());

        Shape clonedShape3 = (Shape) ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());
    }

}
