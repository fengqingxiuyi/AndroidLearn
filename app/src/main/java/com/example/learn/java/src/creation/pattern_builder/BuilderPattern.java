package com.example.learn.java.src.creation.pattern_builder;

import com.example.learn.java.src.creation.pattern_builder.meal.Meal;
import com.example.learn.java.src.creation.pattern_builder.meal.MealBuilder;

/**
 * @author fqxyi
 * @desc BuilderPattern 使用 MealBuilder 来演示建造者模式（Builder Pattern）。
 * @date 2018/7/24
 */
public class BuilderPattern {

    public static void main(String[] args) {
        MealBuilder mealBuilder = new MealBuilder();

        Meal vegMeal = mealBuilder.prepareVegMeal();
        System.out.println("Veg Meal");
        vegMeal.showItems();
        System.out.println("Total Cost: " + vegMeal.getCost());

        Meal nonVegMeal = mealBuilder.prepareNonVegMeal();
        System.out.println("\n\nNon-Veg Meal");
        nonVegMeal.showItems();
        System.out.println("Total Cost: " + nonVegMeal.getCost());
    }

}
