package com.example.learn.java.src.creation.pattern_singleton.single;

/**
 * @author fqxyi
 * @desc 单例类
 * <p>
 * 懒汉式，线程不安全
 * <p>
 * 是否 Lazy 初始化：是
 * 是否多线程安全：否
 * 实现难度：易
 * 描述：这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。因为没有加锁 synchronized，所以严格意义上它并不算单例模式。
 * 这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作。
 * @date 2018/7/24
 */
public class SingleObject1 {

    private static SingleObject1 instance;

    private SingleObject1() {
    }

    public static SingleObject1 getInstance() {
        if (instance == null) {
            instance = new SingleObject1();
        }
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World!");
    }

}
