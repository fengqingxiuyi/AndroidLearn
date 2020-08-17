package com.example.learn.single.java;

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
        System.out.println("java class SingleHungry single");
    }

}
