package com.example.learn.single.java;

import com.example.utils.LogUtil;

/**
 * @author fqxyi
 * @date 2020/8/17
 * 饿汉式，在类加载的时候进行初始化，类加载只会执行一次
 */
public class SingleHungry {

    private SingleHungry() {

    }

    private static SingleHungry instance = new SingleHungry();

    public static SingleHungry getInstance() {
        return instance;
    }

    public void single() {
        LogUtil.i("java class SingleHungry single");
    }

}
