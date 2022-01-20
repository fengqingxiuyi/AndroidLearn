package com.example.learn.java.src.structure.pattern_bridge;

import com.example.learn.java.src.structure.pattern_bridge.draw.impl.GreenCircle;
import com.example.learn.java.src.structure.pattern_bridge.draw.impl.RedCircle;
import com.example.learn.java.src.structure.pattern_bridge.shape.Shape;
import com.example.learn.java.src.structure.pattern_bridge.shape.impl.Circle;

/**
 * @author fqxyi
 * @desc 使用 Shape 和 DrawAPI 类画出不同颜色的圆
 * @date 2018/7/24
 */
public class BridgePattern {

    public static void main(String[] args) {
        Shape redCircle = new Circle(100, 100, 10, new RedCircle());
        Shape greenCircle = new Circle(100, 100, 10, new GreenCircle());

        redCircle.draw();
        greenCircle.draw();
    }

}
