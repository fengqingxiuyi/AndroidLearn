package com.example.learn.java.src.creation.pattern_builder.item.impl.drink;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/24
 */
public class Pepsi extends Drink {

    @Override
    public float price() {
        return 35.0f;
    }

    @Override
    public String name() {
        return "Pepsi";
    }
}