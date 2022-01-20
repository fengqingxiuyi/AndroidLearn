package com.example.learn.java.src.creation.pattern_builder.item.impl.drink;

import com.example.learn.java.src.creation.pattern_builder.item.IItem;
import com.example.learn.java.src.creation.pattern_builder.pack.IPack;
import com.example.learn.java.src.creation.pattern_builder.pack.impl.Bottle;

/**
 * @author fqxyi
 * @desc 创建实现 Item 接口的抽象类，该类提供了默认的功能。
 * @date 2018/7/24
 */
public abstract class Drink implements IItem {

    @Override
    public IPack pack() {
        return new Bottle();
    }

    @Override
    public abstract float price();

}
