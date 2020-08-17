package com.example.learn.single.java;

/**
 * @author fqxyi
 * @date 2020/8/17
 * 懒汉式，双检锁
 */
public class SingleDoubleCheck {

    private SingleDoubleCheck() {

    }

    private static volatile SingleDoubleCheck instance;

    public static SingleDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (SingleDoubleCheck.class) {
                if (instance == null) {
                    instance = new SingleDoubleCheck();
                }
            }
        }
        return instance;
    }

    public void single() {
        System.out.println("java class SingleDoubleCheck single");
    }

}
