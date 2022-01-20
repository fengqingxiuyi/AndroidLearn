package com.example.learn.java.src.creation.pattern_builder.item.impl.drink;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/24
 */
public class Coke extends Drink {

    @Override
    public float price() {
        return 30.0f;
    }

    @Override
    public String name() {
        return "Coke";
    }
}