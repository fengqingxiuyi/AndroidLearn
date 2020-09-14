package com.example.learn.java.src.structure.pattern_bridge.draw.impl;

import com.example.learn.java.src.structure.pattern_bridge.draw.DrawAPI;

/**
 * @author ShenBF
 * @desc 实现了 DrawAPI 接口的实体桥接实现类
 * @date 2018/7/24
 */
public class GreenCircle implements DrawAPI {

    @Override
    public void drawCircle(int radius, int x, int y) {
        System.out.println("Drawing Circle[ color: green, radius: "
                + radius + ", x: " + x + ", " + y + "]");
    }

}