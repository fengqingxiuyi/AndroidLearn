package com.example.learn.java.src.creation.pattern_singleton.single;

/**
 * @author ShenBF
 * @desc 单例类
 * <p>
 * 双检锁/双重校验锁（DCL，即 double-checked locking）
 * <p>
 * JDK 版本：JDK1.5 起
 * 是否 Lazy 初始化：是
 * 是否多线程安全：是
 * 实现难度：较复杂
 * 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 * getInstance() 的性能对应用程序很关键。
 * @date 2018/7/24
 */
public class SingleObject4 {

    private volatile static SingleObject4 instance;

    private SingleObject4() {
    }

    public static SingleObject4 getInstance() {
        if (instance == null) {
            synchronized (SingleObject4.class) {
                if (instance == null) {
                    instance = new SingleObject4();
                }
            }
        }
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World!");
    }

}
