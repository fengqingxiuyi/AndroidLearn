package com.example.learn.single.java;

/**
 * @author fqxyi
 * @date 2020/8/17
 * 懒汉式，调用getInstance函数的时候才会去加载SingleInnerHolder这个静态内部类，且只会加载一次
 */
public class SingleInner {

    private SingleInner () {

    }

    public static class SingleInnerHolder {
        private static SingleInner instance = new SingleInner();
    }

    public static SingleInner getInstance() {
        return SingleInnerHolder.instance;
    }

    public void single() {
        System.out.println("java class SingleInner single");
    }

}
