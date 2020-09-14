package com.example.learn.java.src.creation.pattern_builder.meal;

import com.example.learn.java.src.creation.pattern_builder.item.IItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBF
 * @desc 创建一个 Meal 类，带有上面定义的 Item 对象。
 * @date 2018/7/24
 */
public class Meal {

    private List<IItem> items = new ArrayList<IItem>();

    public void addItem(IItem item) {
        items.add(item);
    }

    public float getCost() {
        float cost = 0.0f;
        for (IItem item : items) {
            cost += item.price();
        }
        return cost;
    }

    public void showItems() {
        for (IItem item : items) {
            System.out.print("Item : " + item.name());
            System.out.print(", Pack : " + item.pack().pack());
            System.out.println(", Price : " + item.price());
        }
    }

}
