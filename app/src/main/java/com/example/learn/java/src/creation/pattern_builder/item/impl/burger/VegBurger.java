package com.example.learn.java.src.creation.pattern_builder.item.impl.burger;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/24
 */
public class VegBurger extends Burger {

    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "Veg Burger";
    }

}
