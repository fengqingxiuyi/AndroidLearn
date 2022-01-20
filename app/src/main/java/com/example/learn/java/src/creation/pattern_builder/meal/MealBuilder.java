package com.example.learn.java.src.creation.pattern_builder.meal;

import com.example.learn.java.src.creation.pattern_builder.item.impl.burger.ChickenBurger;
import com.example.learn.java.src.creation.pattern_builder.item.impl.burger.VegBurger;
import com.example.learn.java.src.creation.pattern_builder.item.impl.drink.Coke;
import com.example.learn.java.src.creation.pattern_builder.item.impl.drink.Pepsi;

/**
 * @author fqxyi
 * @desc 创建一个 MealBuilder 类，实际的 builder 类负责创建 Meal 对象。
 * @date 2018/7/24
 */
public class MealBuilder {

    public Meal prepareVegMeal() {
        Meal meal = new Meal();
        meal.addItem(new VegBurger());
        meal.addItem(new Coke());
        return meal;
    }

    public Meal prepareNonVegMeal() {
        Meal meal = new Meal();
        meal.addItem(new ChickenBurger());
        meal.addItem(new Pepsi());
        return meal;
    }

}
