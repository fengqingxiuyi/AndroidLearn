package com.example.learn.java.src.structure.pattern_bridge.shape;

import com.example.learn.java.src.structure.pattern_bridge.draw.DrawAPI;

/**
 * @author fqxyi
 * @desc 使用 DrawAPI 接口创建抽象类 Shape
 * @date 2018/7/24
 */
public abstract class Shape {

    protected DrawAPI drawAPI;

    protected Shape(DrawAPI drawAPI) {
        this.drawAPI = drawAPI;
    }

    public abstract void draw();

}
