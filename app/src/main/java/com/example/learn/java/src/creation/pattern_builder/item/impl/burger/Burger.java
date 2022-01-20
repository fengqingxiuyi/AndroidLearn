package com.example.learn.java.src.creation.pattern_builder.item.impl.burger;

import com.example.learn.java.src.creation.pattern_builder.item.IItem;
import com.example.learn.java.src.creation.pattern_builder.pack.IPack;
import com.example.learn.java.src.creation.pattern_builder.pack.impl.Wrapper;

/**
 * @author fqxyi
 * @desc 创建实现 Item 接口的抽象类，该类提供了默认的功能。
 * @date 2018/7/24
 */
public abstract class Burger implements IItem {

    @Override
    public IPack pack() {
        return new Wrapper();
    }

    @Override
    public abstract float price();

}
