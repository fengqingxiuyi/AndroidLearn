package com.example.learn.java.src.creation.pattern_builder.item.impl.burger;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/24
 */
public class ChickenBurger extends Burger {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }

}
