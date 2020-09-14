package com.example.learn.java.src.creation.pattern_builder.item;

import com.example.learn.java.src.creation.pattern_builder.pack.IPack;

/**
 * @author ShenBF
 * @desc 食物条目接口
 * @date 2018/7/24
 */
public interface IItem {

    String name();
    IPack pack();
    float price();

}
